package ru.skillbox.socialnetwork.messages.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.socialnetwork.messages.client.dto.AccountDto;

/**
 * Below two options of working with Users micro service:
 * - direct;
 * - thought Gateway
 */

@FeignClient(name = "users-service", url = "${gtw.users-service}", configuration = FeignSupportConfig.class)
//@FeignClient(name = "users-service", url = "${users-service.url}", configuration = FeignSupportConfig.class)
public interface UsersClient {

	@GetMapping(
			value = "/account",
			produces = {"application/json"})
	AccountDto getUserDetails(@RequestParam String email);

	@GetMapping(
			value = "/{id}/account",
			produces = {"application/json"})
	AccountDto getUserDetailsById(@RequestParam Long id);

}
