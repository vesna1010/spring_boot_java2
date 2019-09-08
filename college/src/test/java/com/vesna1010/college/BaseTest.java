package com.vesna1010.college;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public abstract class BaseTest {

	public static final Sort SORT = Sort.by(Order.asc("name"), Order.asc("id"));
	public static final Pageable PAGEABLE = PageRequest.of(0, 10, Direction.ASC, "id");

	public byte[] getPhoto() {
		File file = new File("src\\test\\java\\image\\image.jpg");
		byte[] photo = null;

		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			photo = new byte[(int) file.length()];
			is.read(photo);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return photo;
	}

}
