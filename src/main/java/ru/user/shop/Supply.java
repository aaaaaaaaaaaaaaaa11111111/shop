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
public class Supply {
	private int id;
	@NonNull
	private double price;
	@NonNull
	private Date deliveryDate;
	@NonNull
	private int supplierId, warehouseId;

	protected Supply() {
	}
}
