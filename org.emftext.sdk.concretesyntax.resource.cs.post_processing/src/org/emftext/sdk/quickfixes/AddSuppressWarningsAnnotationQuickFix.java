package org.emftext.sdk.quickfixes;

import org.emftext.sdk.concretesyntax.Annotable;
import org.emftext.sdk.concretesyntax.Annotation;
import org.emftext.sdk.concretesyntax.AnnotationType;
import org.emftext.sdk.concretesyntax.ConcretesyntaxFactory;
import org.emftext.sdk.concretesyntax.KeyValuePair;
import org.emftext.sdk.concretesyntax.resource.cs.CsEProblemSeverity;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsAnalysisProblemType;
import org.emftext.sdk.concretesyntax.resource.cs.mopp.CsQuickFix;

/**
 * A quick fix that adds a @SuppressWarnings annotation for a certain
 * problem type.
 */
public class AddSuppressWarningsAnnotationQuickFix extends CsQuickFix {

	private Annotable annotableElement;
	private CsAnalysisProblemType problemType;

	public AddSuppressWarningsAnnotationQuickFix(Annotable annotableElement,
			CsAnalysisProblemType problemType) {
		// TODO set image for this quick fix
		super("Add @" + AnnotationType.SUPPRESS_WARNINGS.getLiteral() + "(" + problemType.getName() + ")", null, annotableElement);
		// only warnings can be suppressed
		assert problemType.getProblemSeverity() == CsEProblemSeverity.WARNING;
		this.annotableElement = annotableElement;
		this.problemType = problemType;
	}

	@Override
	public void applyChanges() {
		Annotation annotation = ConcretesyntaxFactory.eINSTANCE.createAnnotation();
		annotation.setType(AnnotationType.SUPPRESS_WARNINGS);
		KeyValuePair parameter = ConcretesyntaxFactory.eINSTANCE.createKeyValuePair();
		parameter.setKey(problemType.getName());
		annotation.getParameters().add(parameter);
		annotableElement.getAnnotations().add(annotation);
	}
}
