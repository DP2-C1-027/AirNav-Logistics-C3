/*
 * AuthenticatedCustomersCreateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.customers;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Customers;

@GuiService
public class AuthenticatedCustomersCreateService extends AbstractGuiService<Authenticated, Customers> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedCustomersRepository repository;

	// AbstractService<Authenticated, Customers> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Customers.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customers object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Customers();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Customers object) {
		assert object != null;

		super.bindObject(object, "code", "phone", "physicalAddress", "city", "country", "earnedPoints");
	}

	@Override
	public void validate(final Customers object) {
		assert object != null;
	}

	@Override
	public void perform(final Customers object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Customers object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "code", "phone", "physicalAddress", "city", "country", "earnedPoints");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
