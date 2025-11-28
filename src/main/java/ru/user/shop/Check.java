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
public class Check {
	private int id;
	@NonNull
	private int shopId, customerId;
	@NonNull
	private Date purchaseDate;
	@NonNull
	private double discount;

	protected Check() {
	}

}
