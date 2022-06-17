package zbwmsj

import com.bjrxkj.core.BaseUser

class CheckUserFilters {

    def filters = {
        justApi(url:'api/*') {
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
