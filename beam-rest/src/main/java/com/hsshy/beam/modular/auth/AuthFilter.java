package com.hsshy.beam.modular.auth;
import com.hsshy.beam.config.properties.JwtProperties;
import com.hsshy.beam.modular.auth.util.JwtTokenUtil;
import com.hsshy.common.enumeration.RetEnum;
import com.hsshy.common.support.StrKit;
import com.hsshy.common.utils.R;
import com.hsshy.common.utils.RenderUtil;
import io.jsonwebtoken.JwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:04
 */
public class AuthFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.error(jwtProperties.getAuthPath());
        if (request.getServletPath().equals("/" + jwtProperties.getAuthPath())) {
            chain.doFilter(request, response);
            return;
        }

        String referer = request.getHeader("Referer");
        if(StrKit.isNotEmpty(referer) && (referer.contains("swagger") || referer.contains("apiDoc"))){
            chain.doFilter(request, response);
            return;
        }
        if(
            request.getServletPath().contains("swagger") ||
            request.getServletPath().contains("api-docs") ||
            request.getServletPath().contains("webjars") ||
            request.getServletPath().contains("configuration")){
            chain.doFilter(request, response);
            return;
        }

        final String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);

            //验证token是否过期,包含了验证jwt是否正确
            try {
                boolean flag = jwtTokenUtil.isTokenExpired(authToken);
                if (flag) {

                    RenderUtil.renderJson(response,  R.fail(RetEnum.TOKEN_EXPIRED.getRet(), RetEnum.TOKEN_EXPIRED.getMsg()));
                    return;
                }
            } catch (JwtException e) {
                //有异常就是token解析失败
                RenderUtil.renderJson(response, R.fail(RetEnum.TOKEN_ERROR.getRet(), RetEnum.TOKEN_ERROR.getMsg()));
                return;
            }
        } else {
            //header没有带Bearer字段
            RenderUtil.renderJson(response, R.fail(RetEnum.TOKEN_ERROR.getRet(), RetEnum.TOKEN_ERROR.getMsg()));
            return;
        }
        chain.doFilter(request, response);
    }
}