package de.fraunhofer.aisec.cpg.enhancements;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.fraunhofer.aisec.cpg.TestUtils;
import de.fraunhofer.aisec.cpg.graph.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReadWriteDFGTest {

  @Test
  void testCompoundOperatorDFG() throws Exception {
    Path topLevel = Path.of("src", "test", "resources", "dfg");
    List<TranslationUnitDeclaration> result =
        TestUtils.analyze(
            List.of(topLevel.resolve("compoundoperator.cpp").toFile()), topLevel, true);

    BinaryOperator rwCompoundOperator =
        TestUtils.findByUniqueName(TestUtils.subnodesOfType(result, BinaryOperator.class), "+=");
    DeclaredReferenceExpression expression =
        TestUtils.findByUniqueName(
            TestUtils.subnodesOfType(result, DeclaredReferenceExpression.class), "i");
    VariableDeclaration variableDeclaration =
        TestUtils.findByUniqueName(
            TestUtils.subnodesOfType(result, VariableDeclaration.class), "i");

    Set<Node> prevDFGOperator = rwCompoundOperator.getPrevDFG();
    Set<Node> nextDFGOperator = rwCompoundOperator.getNextDFG();

    assertTrue(prevDFGOperator.contains(expression));
    assertTrue(nextDFGOperator.contains(expression));

    Set<Node> prevDFGDeclaredReferenceExpression = expression.getPrevDFG();
    Set<Node> nextDFGDeclaredReferenceExpression = expression.getNextDFG();

    assertTrue(prevDFGDeclaredReferenceExpression.contains(variableDeclaration));
    assertTrue(nextDFGDeclaredReferenceExpression.contains(variableDeclaration));
  }

  @Test
  void testUnaryOperatorDFG() throws Exception {
    Path topLevel = Path.of("src", "test", "resources", "dfg");
    List<TranslationUnitDeclaration> result =
        TestUtils.analyze(List.of(topLevel.resolve("unaryoperator.cpp").toFile()), topLevel, true);

    UnaryOperator rwUnaryOperator =
        TestUtils.findByUniqueName(TestUtils.subnodesOfType(result, UnaryOperator.class), "++");
    DeclaredReferenceExpression expression =
        TestUtils.findByUniqueName(
            TestUtils.subnodesOfType(result, DeclaredReferenceExpression.class), "i");
    VariableDeclaration variableDeclaration =
        TestUtils.findByUniqueName(
            TestUtils.subnodesOfType(result, VariableDeclaration.class), "i");

    Set<Node> prevDFGOperator = rwUnaryOperator.getPrevDFG();
    Set<Node> nextDFGOperator = rwUnaryOperator.getNextDFG();

    assertTrue(prevDFGOperator.contains(expression));
    assertTrue(nextDFGOperator.contains(expression));

    Set<Node> prevDFGDeclaredReferenceExpression = expression.getPrevDFG();
    Set<Node> nextDFGDeclaredReferenceExpression = expression.getNextDFG();

    assertTrue(prevDFGDeclaredReferenceExpression.contains(variableDeclaration));
    assertTrue(nextDFGDeclaredReferenceExpression.contains(variableDeclaration));
  }
}