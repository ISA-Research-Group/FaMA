/**
 *  This file is part of FaMaTS.
 *
 *  FaMaTS is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  FaMaTS is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with FaMaTS.  If not, see <http://www.gnu.org/licenses/>.
 */
package co.icesi.i2t.Choco3Reasoner.tests.simple.questions;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import co.icesi.i2t.Choco3Reasoner.simple.questions.Choco3ProductsQuestion;
import co.icesi.i2t.FAMA.TestSuite2.TestLoader;
import co.icesi.i2t.FAMA.TestSuite2.reasoners.AbstractReasonerQuestionTestCase;
import es.us.isa.FAMA.models.FAMAfeatureModel.Feature;
import es.us.isa.FAMA.models.featureModel.Product;

/**
 * Test case for the Products question in the Choco 3 Reasoner.
 * 
 * @author Andrés Paz, I2T Research Group, Icesi University, Cali - Colombia
 * @version 1.0, October 2014
 */
public class Choco3ProductsQuestionTestCase extends
		AbstractReasonerQuestionTestCase {

	/**
	 * Test configuration file path
	 */
	private static final String TEST_CONFIG_FILE = "test-resources/Choco3TestConfig.xml";
	
	/**
	 * Question name
	 */
	private static final String QUESTION = "Products";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.icesi.i2t.FAMA.TestSuite2.reasoners#AbstractReasonerQuestionTestCase(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	public Choco3ProductsQuestionTestCase(String variabilityModelPath,
			String input, String expectedOutput) {
		super(variabilityModelPath, input, expectedOutput);
	}

	/**
	 * Loads the tests for the number of products question. Tests are specified
	 * in an XML file with the information of the variability models to test and
	 * the questions to be asked with their expected output.
	 * 
	 * @return A collection of 3-tuples (feature model, input, expected output).
	 * @throws FileNotFoundException
	 *             If the test configuration file is not found.
	 * @throws Exception
	 *             If any other errors occur.
	 */
	@Parameters
	public static Collection<?> loadTests() throws FileNotFoundException,
			Exception {
		return Arrays.asList(TestLoader.loadQuestionTests(TEST_CONFIG_FILE, QUESTION));
	}
	/**
	 * Test method for {@link co.icesi.i2t.Choco3Reasoner.simple.questions.Choco3ProductsQuestion#getAllProducts()}.
	 */
	@Test
	public void testGetAllProducts() {
		System.out.println("\n[TEST] Products");
		
		// Load the variability model that will be evaluated during the test.
		variabilityModel = questionTrader.openFile(variabilityModelPath);
		questionTrader.setVariabilityModel(variabilityModel);
		System.out.println("For model: \"" + variabilityModelPath + "\"");
		
		// Create the question instance to be tested.
		Choco3ProductsQuestion choco3ProductsQuestion = (Choco3ProductsQuestion) questionTrader.createQuestion(QUESTION);
		
		if (choco3ProductsQuestion != null) {
			try {
				// Ask the question.
				questionTrader.ask(choco3ProductsQuestion);
				
				Comparator<Product> comparator = new Comparator<Product>() {
				    public int compare(Product c1, Product c2) {
				        return c2.toString().compareTo(c1.toString());
				    }
				};
				
				// Retrieve the result.
				LinkedList<Product> output = (LinkedList<Product>) choco3ProductsQuestion.getAllProducts();
				Collections.sort(output, comparator);
				
				if (!expectedOutput.equals("")) {
					LinkedList<Product> expectedProducts = new LinkedList<Product>();
					String[] expectedOutputs = expectedOutput.split(";");
					for (int i = 0; i < expectedOutputs.length; i++) {
						String expectedProduct = expectedOutputs[i];
						String[] expectedFeatures = expectedProduct.split(":");
						Product product = new Product();
						for (int j = 0; j < expectedFeatures.length; j++) {
							String expectedFeature = expectedFeatures[j];
							Feature feature = new Feature(expectedFeature);
							product.addFeature(feature);
						}
						expectedProducts.add(product);
					}
					Collections.sort(expectedProducts, comparator);

					System.out.println("Expected products: " + expectedProducts);
					System.out.println("Obtained products: " + output);
					
					// Compare the result against an expected output value.
					assertEquals(expectedProducts, output);
					System.out.println("[INFO] Test case passed");
				
				} else {
					System.out.println("[INFO] No expected output for test case.");
					System.out.println("Obtained products: " + output);
					System.out.println("[INFO] Test case ignored");
				}
			} catch (AssertionError e) {
				System.out.println("[INFO] Test case failed. Cause: " + e.getMessage());
				throw e;
			} catch (Exception e) {
				System.out.println("[INFO] Test case failed. Cause: " + e.getMessage());
				throw e;
			}
		} else {
			fail("Current reasoner does not accept this operation.");
			System.out.println("[INFO] Current reasoner does not accept this operation.");
		}
	}

}
