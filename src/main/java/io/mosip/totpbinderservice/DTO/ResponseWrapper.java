/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.mosip.totpbinderservice.DTO;

import java.util.ArrayList;
import java.util.List;

public class ResponseWrapper<T> {

    public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public T getResponse() {
		return response;
	}
	public void setResponse(T response) {
		this.response = response;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	private String responseTime;
    private T response;
    private List<Error> errors = new ArrayList<>();
}
