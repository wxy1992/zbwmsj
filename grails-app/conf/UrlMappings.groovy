class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/api/trade/$action"{
            controller="tradeApi"
            action=action
        }


        "/api/cms/$action"{
            controller="cmsApi"
            action=action
        }

        "/"(controller: "cms",action: "workSpace")
        "404"(view:'/error404')
        "500"(view:'/error')
	}
}
