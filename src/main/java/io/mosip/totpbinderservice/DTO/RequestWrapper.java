/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.totpbinderservice.DTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RequestWrapper<T> {

    private String requestTime;

    public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public T getRequest() {
		return request;
	}

	public void setRequest(T request) {
		this.request = request;
	}

	@NotNull(message = "invalid request")
    @Valid
    private T request;
}
