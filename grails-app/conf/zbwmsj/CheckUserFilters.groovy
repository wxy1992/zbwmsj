package zbwmsj

import com.wmsj.core.BaseUser
import com.wmsj.core.WxUser

class CheckUserFilters {

    def filters = {
        justApi(controller:'tradeApi',action:"*") {
            before = {
                def token=request.getHeader("Authorization")?.replace("Bearer ","");
                if(!token) token="no1yozy";
//                println  "------------------token：${token}-----------------------------------"
                if(token){
                    def wxUser= WxUser.findByOpenId(token);
                    if(wxUser){
//                        println  "------------------token：${wxUser}-----------------------------------"
                        request.setAttribute("wxUser",wxUser);
                    }else{
                        render "非法用户";
                        return false;
                    }
                    return true;
                }else{
                    render "请求头缺少token";
                    return false;
                }
            }
            after = { Map model ->
                return true;
            }
            afterView = { Exception e ->

            }
        }
    }
}
