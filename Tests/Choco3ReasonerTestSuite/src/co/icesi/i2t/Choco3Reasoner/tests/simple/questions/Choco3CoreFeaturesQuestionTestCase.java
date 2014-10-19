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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import co.icesi.i2t.Choco3Reasoner.simple.questions.Choco3CoreFeaturesQuestion;
import co.icesi.i2t.FAMA.TestSuite2.TestLoader;
import co.icesi.i2t.FAMA.TestSuite2.reasoners.AbstractReasonerQuestionTestCase;
import es.us.isa.FAMA.models.FAMAfeatureModel.Feature;
import es.us.isa.FAMA.models.featureModel.GenericFeature;

/**
 * Test case for the Core Features question in the Choco 3 Reasoner.
 * 
 * @author Andrés Paz, I2T Research Group, Icesi University, Cali - Colombia
 * @version 1.0, October 2014
 */
public class Choco3CoreFeaturesQuestionTestCase extends AbstractReasonerQuestionTestCase {

	/**
	 * Test configuration file path
	 */
	private static final String TEST_CONFIG_FILE = "test-resources/Choco3TestConfig.xml";

	/**
	 * Question name
	 */
	private static final String QUESTION = "CoreFeatures";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * co.icesi.i2t.FAMA.TestSuite2.reasoners#AbstractReasonerQuestionTestCase(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	public Choco3CoreFeaturesQuestionTestCase(String variabilityModelPath,
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
	 * Test method for
	 * {@link co.icesi.i2t.Choco3Reasoner.simple.questions.Choco3CoreFeaturesQuestion#getCoreFeats()}
	 * .
	 */
	@Test
	public void testGetCoreFeats() {
		System.out.println("\n[TEST] Core Features");
		
		// Load the variability model that will be evaluated during the test.
		variabilityModel = questionTrader.openFile(variabilityModelPath);
		questionTrader.setVariabilityModel(variabilityModel);
		System.out.println("For model: \"" + variabilityModelPath + "\"");
		
		Choco3CoreFeaturesQuestion choco3CoreFeaturesQuestion = (Choco3CoreFeaturesQuestion) questionTrader.createQuestion(QUESTION);
		
		if (choco3CoreFeaturesQuestion != null) {
			questionTrader.ask(choco3CoreFeaturesQuestion);
			try {
				Comparator<GenericFeature> comparator = new Comparator<GenericFeature>() {
				    public int compare(GenericFeature c1, GenericFeature c2) {
				        return c2.getName().compareTo(c1.getName());
				    }
				};
				
				ArrayList<GenericFeature> output = (ArrayList<GenericFeature>) choco3CoreFeaturesQuestion.getCoreFeats();
				Collections.sort(output, comparator);
				
				if (!expectedOutput.equals("")) {
					String[] expectedOutputs = expectedOutput.split(";");
					ArrayList<GenericFeature> expectedCoreFeatures = new ArrayList<GenericFeature>();
					for (int i = 0; i < expectedOutputs.length; i++) {
						Feature expectedCoreFeature = new Feature(expectedOutputs[i]);
						expectedCoreFeatures.add(expectedCoreFeature);
					}
					Collections.sort(expectedCoreFeatures, comparator);

					System.out.println("Expected core features: " + expectedCoreFeatures);
					System.out.println("Obtained core features: " + output);
					
					assertEquals(expectedCoreFeatures, output);
					System.out.println("[INFO] Test case passed");
				} else {
					System.out.println("[INFO] No expected output for test case.");
					System.out.println("Obtained core features: " + output);
					System.out.println("[INFO] Test case ignored");
				}
			} catch (AssertionError e) {
				System.out.println("[INFO] Test case failed");
				throw e;
			}
		} else {
			fail("Current reasoner does not accept this operation.");
			System.out.println("[INFO] Current reasoner does not accept this operation.");
		}
	}

}
