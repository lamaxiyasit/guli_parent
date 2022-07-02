package com.atguigu.guli.service.ucenter.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.common.base.util.JwtInfo;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.common.base.util.MD5;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.atguigu.guli.service.ucenter.mapper.MemberMapper;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-05-23
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo registerVo) {
        //校验参数
        String nickName=registerVo.getNickname();
        String mobile=registerVo.getMobile();
        String password=registerVo.getPassword();
        String code=registerVo.getCode();

        if(StringUtils.isEmpty(mobile)
            ||!FormUtils.isMobile(mobile)
            ||StringUtils.isEmpty(nickName)
            ||StringUtils.isEmpty(password)
            ||StringUtils.isEmpty(code)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        String checkCode=(String)redisTemplate.opsForValue().get(mobile);
        if(!code.equals(checkCode)){
            throw new GuliException(ResultCodeEnum.CODE_ERROR);
        }

        //是否被注册
        QueryWrapper<Member> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Integer count=baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        //注册
        Member member=new Member();
        member.setNickname(nickName);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://guli-file-19970812.oss-cn-shanghai.aliyuncs.com/avatar/2022/04/13/default.jpg");
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile=loginVo.getMobile();
        String password=loginVo.getPassword();

        //校验参数是否合法
        if(StringUtils.isEmpty(mobile)
            ||!FormUtils.isMobile(mobile)
            ||StringUtils.isEmpty(password)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        //校验手机号是否存在
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Member member=baseMapper.selectOne(queryWrapper);
        if(member == null){
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //校验密码是否正确
        if(!MD5.encrypt(password).equals(member.getPassword())){
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        //校验用户是否被禁用
        if(member.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //登录：生成token
        JwtInfo info = new JwtInfo();
        info.setId(member.getId());
        info.setNickname(member.getNickname());
        info.setAvatar(member.getAvatar());
        String jwtToken = JwtUtils.getJwtToken(info,1800);
        return jwtToken;
    }
}
