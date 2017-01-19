package com.github.gsteam5.liferay.lastlogin;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author GSTeam5
 */
@ApplicationPath("/")
@Component(
	immediate = true, property = {"jaxrs.application=true"},
	service = Application.class
)
public class LastLoginService extends Application {

	@Override
	public Set<Object> getSingletons() {
		return Collections.singleton((Object)this);
	}

	@GET
	@Path("/newusers")
	@Produces("text/plain")
	public String getUsers() {
		
		List<User> users = _userLocalService.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
		
		int count = 0;
		
		for ( User user : users ) {
			if ( user.getCreateDate().getTime() + 30000 > System.currentTimeMillis()) {
				count++;
			}
 		}
		
		return String.valueOf(count);
	}

	@Reference
	private volatile UserLocalService _userLocalService;
}