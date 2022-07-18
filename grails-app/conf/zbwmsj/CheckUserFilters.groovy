package zbwmsj

import com.wmsj.core.BaseUser
import com.wmsj.core.WxUser

class CheckUserFilters {

    def filters = {
        justApi(controller:'tradeApi',action:"*") {
            before = {
                def userid=request.getHeader("userid");
                println  "---------userid：${userid}------------------"
                if(userid){
                    def wxUser= WxUser.get(userid);
                    if(wxUser){
                        println  "---------wxUser：${wxUser}-----------"
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
