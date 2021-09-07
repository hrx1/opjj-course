package hr.fer.zemris.java.hw05.db;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConditionalExpressionTest {
	ConditionalExpression expr;
	
	@Before
	public void setUp() throws Exception {
		expr = new ConditionalExpression(FieldValueGetters.LAST_NAME, "Bos*",
				ComparisonOperators.LIKE);

	}

	@Test
	public void test() {
		StudentRecord record = new StudentRecord("000", "Boasnic", "nemam", "2");
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record),
				expr.getStringLiteral());
		
		assertTrue(recordSatisfies);
	}

}
