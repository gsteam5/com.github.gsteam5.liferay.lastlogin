package com.github.gsteam5.liferay.lastlogin;

import com.github.gsteam5.liferay.usergen.RandomUserHelper;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author GSTeam5
 */
@ApplicationPath("/")
@Component(immediate = true, property = { "jaxrs.application=true" }, service = Application.class)
public class LastLoginService extends Application {

	@Override
	public Set<Object> getSingletons() {
		return Collections.singleton((Object) this);
	}

	@GET
	@Path("/newusers")
	@Produces("text/plain")
	public String getUsers() {

		List<User> users = _userLocalService.getUsers(QueryUtil.ALL_POS, QueryUtil.ALL_POS);

		int count = 0;

		for (User user : users) {
			if (user.getCreateDate().getTime() + 30000 > System.currentTimeMillis()) {
				count++;
			}
		}

		return String.valueOf(count);
	}

	@GET
	@Path("/createuser/{amount}")
	public String createUsers(@PathParam("amount") long amount) {
		
		long companyId = _portal.getDefaultCompanyId();
		
		if (amount > 10) {
			amount = 10;
		}
		
		int count = 0;
		try {

			for (int i = 0; i < amount; i++) {
				RandomUserHelper.createRandomUser(_userLocalService, companyId);
				count++;
			}
		} catch (Exception e) {

		}

		return String.valueOf(count);
	}

	@Reference
	private volatile UserLocalService _userLocalService;

	@Reference
	private volatile Portal _portal;
}