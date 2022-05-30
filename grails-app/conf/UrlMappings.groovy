class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
        "/news/picture/${id}.jpg"{
            controller="news"
            action="picture"
        }
        "/"(controller: "cms",action: "workSpace")
        "404"(view:'/error')
        "500"(view:'/error')
	}
}
