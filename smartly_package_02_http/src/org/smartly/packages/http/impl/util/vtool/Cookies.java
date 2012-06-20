package org.smartly.packages.http.impl.util.vtool;


import org.eclipse.jetty.util.resource.Resource;
import org.json.JSONObject;
import org.smartly.commons.util.JsonWrapper;
import org.smartly.packages.velocity.impl.vtools.IVLCTool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class Cookies implements IVLCTool {

    public static final String NAME = "cookies";

    public static final String HEADER_SET_COOKIE = "Set-Cookie";
    public static final String HEADER_COOKIE = "Cookie";

    private final Resource _resource;
    private final HttpServletRequest _request;
    private final HttpServletResponse _response;
    private final Map<String, String> __cookies;
    private JSONObject _authCookie;

    private String _langCode;
    private String _userAgent;

    public Cookies(final Resource resource, final HttpServletRequest httprequest, final HttpServletResponse httpresponse) {
        _resource = resource;
        _request = httprequest;
        _response = httpresponse;
        __cookies = new HashMap<String, String>();
    }

    @Override
    public String getName() {
        return NAME;
    }


    public Cookie[] getCookiesArray() {
        return _request.getCookies();
    }

    public Cookie getCookie(final String name) {
        final Cookie[] cookies = this.getCookiesArray();
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie;
            }
        }
        return null;
    }

    public String getCookieValue(final String name) {
        final Cookie cookie = this.getCookie(name);
        return null != cookie ? cookie.getValue() : "";
    }

    public Map<String, String> getCookies() {
        if (__cookies.size() == 0) {
            this.initCookies();
        }
        return __cookies;
    }

    public void addCookie(final Cookie cookie) {
        _response.addCookie(cookie);
        __cookies.put(cookie.getName(), cookie.getValue());
    }

    public void addCookie(final String name, final String value) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        this.addCookie(cookie);
    }

    public boolean hasCookie(final String name) {
        if (__cookies.size() == 0) {
            this.initCookies();
        }
        return __cookies.containsKey(name);
    }

    public JSONObject getAuth() {
        if (null == _authCookie) {
            _authCookie = this.getAuthCookie();
        }
        return _authCookie;
    }


    // ------------------------------------------------------------------------
    //                      p r i v a t e
    // ------------------------------------------------------------------------

    private void initCookies() {
        final Cookie[] cookies = this.getCookiesArray();
        for (final Cookie cookie : cookies) {
            __cookies.put(cookie.getName(), cookie.getValue());
        }
    }


    /**
     * exports.userid = authcookie.userid || exports.userid;
     * exports.accountid = authcookie.accountid || exports.accountid || exports.userid;
     * exports.username = authcookie.username || exports.username;
     * exports.lang = authcookie.lang || exports.lang;
     * exports.currency = authcookie.curr || exports.currency;
     * exports.country = authcookie.country || exports.country;
     * //decimals =  authCookie.userid||decimals;
     * exports.decimalsep = authcookie.sep || exports.decimalsep;
     * exports.discount = authcookie.discount || exports.discount;
     *
     * @return
     */
    private JSONObject getAuthCookie() {
        final JSONObject result = new JSONObject();

        // userid
        if (!this.hasCookie("userid")) {
            this.addCookie("userid", "0");
        }
        JsonWrapper.put(result, "userid", this.getCookieValue("userid"));

        // userid
        if (!this.hasCookie("accountid")) {
            this.addCookie("accountid", "0");
        }
        JsonWrapper.put(result, "accountid", this.getCookieValue("accountid"));

        // accountid
        if (!this.hasCookie("accountid")) {
            this.addCookie("accountid", "0");
        }
        JsonWrapper.put(result, "accountid", this.getCookieValue("accountid"));

        // lang
        if (!this.hasCookie("lang")) {
            this.addCookie("lang", getLangCode());
        }
        JsonWrapper.put(result, "lang", this.getCookieValue("lang"));

        // currency
        if (!this.hasCookie("currency")) {
            this.addCookie("currency", "USD");
        }
        JsonWrapper.put(result, "currency", this.getCookieValue("currency"));

        // country
        if (!this.hasCookie("country")) {
            this.addCookie("country", "US");
        }
        JsonWrapper.put(result, "country", this.getCookieValue("country"));

        // decimalsep
        if (!this.hasCookie("decimalsep")) {
            this.addCookie("decimalsep", ".");
        }
        JsonWrapper.put(result, "decimalsep", this.getCookieValue("decimalsep"));

        // discount
        if (!this.hasCookie("discount")) {
            this.addCookie("discount", "0");
        }
        JsonWrapper.put(result, "discount", this.getCookieValue("discount"));

        // exports.userenabled = null != authcookie.userenabled ? authcookie.userenabled : false;
        if (!this.hasCookie("userenabled")) {
            this.addCookie("userenabled", "false");
        }
        JsonWrapper.put(result, "userenabled", this.getCookieValue("userenabled"));

        return result;
    }

    private String getLangCode() {
        return Req.getLang(_request);
    }
}