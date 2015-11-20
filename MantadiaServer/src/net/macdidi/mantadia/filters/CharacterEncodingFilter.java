package net.macdidi.mantadia.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 設定所有請求與回應的編碼為UTF-8
 * 
 * @author macdidi
 */
@WebFilter(filterName = "CharacterEncodingFilter", 
           urlPatterns = { "/*" }, 
           initParams = {
               @WebInitParam(name = "encoding", value = "UTF-8") 
           })
public class CharacterEncodingFilter implements Filter {

    private String DEFAULT_ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 讀取元件設定的編碼
        String encoding = filterConfig.getInitParameter("encoding");

        if (encoding != null) {
            DEFAULT_ENCODING = encoding;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        // 設定請求與回應的編碼
        request.setCharacterEncoding(DEFAULT_ENCODING);
        response.setCharacterEncoding(DEFAULT_ENCODING);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
    
}
