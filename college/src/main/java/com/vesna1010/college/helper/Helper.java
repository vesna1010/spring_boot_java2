package com.vesna1010.college.helper;

import java.util.ArrayList;
import java.util.List;

public class Helper {

	public static List<Integer> getNumbers(int currentPage, int totalPages) {
		List<Integer> numbers = new ArrayList<>();

		if (totalPages == 0) {
			return numbers;
		}

		for (int i = currentPage - 2; i <= currentPage + 2; i++) {
			numbers.add(i);
		}

		while (numbers.get(0) < 0) {
			for (int i = 0; i < numbers.size(); i++) {
				numbers.set(i, numbers.get(i) + 1);
			}
		}

		while (numbers.get(numbers.size() - 1) >= totalPages) {
			numbers.remove(numbers.size() - 1);
		}

		while (numbers.size() < 5 && numbers.get(0) > 0) {
			numbers.add(0, numbers.get(0) - 1);
		}

		return numbers;
	}

}
