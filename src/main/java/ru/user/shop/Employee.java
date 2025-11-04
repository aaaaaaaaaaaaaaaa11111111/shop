package ru.user.shop;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Employee {

	private int id;
	@NonNull
	private String fullName, email;
	@NonNull
	private long phoneNumber;
	@NonNull
	private Date birthdayDate, dateOfEmployment;
	@NonNull
	private String status;
	@NonNull
	private int shopId;
	@NonNull
	private String photo;
}
