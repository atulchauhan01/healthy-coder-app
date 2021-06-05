/**
 * 
 */
package com.healthycoderapp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author atuchauh
 *
 */
class BMICalculatorTest {
	
	private String environment = "prod";
	
	@ParameterizedTest
	@ValueSource(doubles = {81.0, 83.1})
	@DisplayName(">>> Sample method display name")
	@DisabledOnOs(OS.LINUX)
	void shouldReturnTrueWhenDietRecommended(Double coderWeight) {
		// given
		double weight = coderWeight;
		double height = 1.72;

		// when
		boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

		// then
		assertTrue(dietRecommended);
	}
	
	@ParameterizedTest(name = "Weight={0}, Height={1} ")
	@CsvSource(value = {"81.0, 1.72", "83.1, 1.72"})
	void shouldReturnTrueWhenDietRecommended(Double coderWeight, Double coderHeight) {
		// given
		double weight = coderWeight;
		double height = coderHeight;

		// when
		boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

		// then
		assertTrue(dietRecommended);
	}

	@ParameterizedTest(name = "Weight={0}, Height={1} ")
	@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
	void shouldReturnTrueWhenDietRecommended_DataFromCSV(Double coderWeight, Double coderHeight) {
		// given
		double weight = coderWeight;
		double height = coderHeight;

		// when
		boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

		// then
		assertTrue(dietRecommended);
	}


	@Test
	void shouldReturnTrueWhenDietRecommended() {
		// given
		double weight = 76.4;
		double height = 1.72;

		// when
		boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

		// then
		assertTrue(dietRecommended);
	}

	@Test
	void shouldReturnFalseWhenDietNotRecommended() {
		// given
		double weight = 50.4;
		double height = 1.85;

		// when
		boolean dietRecommended = BMICalculator.isDietRecommended(weight, height);

		// then
		assertFalse(dietRecommended);
	}

	@Test
	void shouldThrowArithmaticExceptionWhenHeightIsZero() {
		// given
		double weight = 50.4;
		double height = 0.0;

		// when
		Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

		// then
		assertThrows(ArithmeticException.class, executable);
	}
	
	@Test
	void should_returnCoderWorstBMICoder_CoderListIsNotEmpty() {
		//given
		List<Coder> coders = new ArrayList<>();
		coders.add(new Coder(1.80, 60.0));
		coders.add(new Coder(1.82, 98.0));
		coders.add(new Coder(1.82, 64.7));
		
		//when
		Coder worstBMICoder = BMICalculator.findCoderWithWorstBMI(coders);
		
		//then
		//If 1st assert fails, 2nd will not execute
		//assertAll should be used in such cases
		assertAll(
				() -> assertEquals(1.82, worstBMICoder.getHeight()),
				() -> assertEquals(98.0, worstBMICoder.getWeight())
			);
		
	}
	
	@Test
	void should_returnCoderWorstBMICoderIn1Ms_CoderListHas10000Elements() {
		//given
		assumeTrue("prod".equals(this.environment));
		List<Coder> coders = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			coders.add(new Coder(1.80+i, 60.0+i));
		}
		
		//when
		Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
		
		//then
		assertTimeout(Duration.ofMillis(50), executable);
		
	}
	
	@Test
	void should_ReturnNullWorstBMICoder_CoderListIsEmpty() {
		//given
		List<Coder> coders = new ArrayList<>();
				
		//when
		Coder worstBMICoder = BMICalculator.findCoderWithWorstBMI(coders);
		
		//then
		assertNull(worstBMICoder);
		
	}
	
	@Test
	void should_ReturnCorrectBMIScoreArray_When_CoderListIsNotEmpty() {
		//given
		List<Coder> coders = new ArrayList<>();
		coders.add(new Coder(1.80, 60.0));
		coders.add(new Coder(1.82, 98.0));
		coders.add(new Coder(1.82, 64.7));
		double[] expected = {18.52, 29.59, 19.53};
						
		//when
		double[] bmiScores = BMICalculator.getBMIScores(coders);
		
		//then
		assertArrayEquals(expected, bmiScores);
		
	}
}















