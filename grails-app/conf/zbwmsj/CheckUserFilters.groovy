package zbwmsj

import com.wmsj.core.BaseUser

class CheckUserFilters {

    def filters = {
        justApi(uri:'/api/*') {
            before = {
                def token=request.getHeader("Authorization")?.replace("Bearer ","");
                println  "------------------token：${token}-----------------------------------"
                if(token){
                    def baseUser= BaseUser.findByUsername(token);
                    if(baseUser){
                        println  "------------------token：${baseUser}-----------------------------------"
                        request.setAttribute("baseUser",baseUser);
                    }else{
                        render "非法用户";
                        return true;
                    }
                    return true;
                }else{
                    render "请求头缺少token";
                    return true;
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
