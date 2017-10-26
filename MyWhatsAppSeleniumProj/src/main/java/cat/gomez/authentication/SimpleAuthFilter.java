package cat.gomez.authentication;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class SimpleAuthFilter implements Filter {
    private SimpleAuth sa;

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        sa = new SimpleAuth();
        // key
        final String cfg_key = filterConfig.getInitParameter("pre-shared-key");
        if (cfg_key == null) {
            throw new ServletException("Invalid key (null) for " + getClass().getName());
        }
        sa.setPreSharedKey(cfg_key);
        // expire
        final String cfg_tokenExpire = filterConfig.getInitParameter("token-expire");
        if (cfg_tokenExpire != null) {
            final int expire = Integer.parseInt(cfg_tokenExpire);
            if (expire > 0) {
                sa.setExpire(expire);
            }
        }
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            final HttpServletRequest req = ((HttpServletRequest) request);
            final HttpServletResponse res = ((HttpServletResponse) response);
            final String header = req.getHeader(SimpleAuth.HTTP_HEADER);
            if (header != null) {
                final String[] sp = header.split(" ");
                // Authentication: torch TOKEN
                if (sp.length == 2) {
                    final String scheme = sp[0], param = sp[1];
                    if (SimpleAuth.SCHEME.equals(scheme)) {
                        if (sa.verify(param)) {
                            chain.doFilter(request, response);
                            return;
                        }
                    }
                }
            }
            if (!res.isCommitted()) {
                final String err = "FORBIDDEN\r\n";
                res.reset();
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.setContentLength(err.length());
                res.setContentType("text/plain");
                res.setCharacterEncoding("ISO-8859-1");
                res.setHeader("Pragma", "no-cache");
                res.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate");
                res.setHeader(getClass().getSimpleName(), "deny");
                res.getWriter().print(err);
                return;
            }
        }
        if (!response.isCommitted()) {
            response.reset();
        }
        throw new ServletException(new UnsupportedOperationException());
    }

    @Override
    public void destroy() {
        sa = null;
    }
}
