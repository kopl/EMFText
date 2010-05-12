package org.emftext.sdk.codegen.resource.generators.grammar;

import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.ECORE_FACTORY;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_CLASS;
import static org.emftext.sdk.codegen.resource.generators.IClassNameConstants.E_STRUCTURAL_FEATURE;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenFeature;
import org.eclipse.emf.ecore.EObject;
import org.emftext.sdk.codegen.IGenerator;
import org.emftext.sdk.codegen.composites.JavaComposite;
import org.emftext.sdk.codegen.composites.StringComposite;
import org.emftext.sdk.codegen.resource.GenerationContext;
import org.emftext.sdk.codegen.resource.GeneratorUtil;
import org.emftext.sdk.codegen.resource.TextResourceArtifacts;
import org.emftext.sdk.codegen.resource.generators.JavaBaseGenerator;
import org.emftext.sdk.codegen.util.NameUtil;
import org.emftext.sdk.concretesyntax.Annotation;
import org.emftext.sdk.concretesyntax.Cardinality;
import org.emftext.sdk.concretesyntax.CardinalityDefinition;
import org.emftext.sdk.concretesyntax.Choice;
import org.emftext.sdk.concretesyntax.CompoundDefinition;
import org.emftext.sdk.concretesyntax.ConcreteSyntax;
import org.emftext.sdk.concretesyntax.Containment;
import org.emftext.sdk.concretesyntax.CsString;
import org.emftext.sdk.concretesyntax.Definition;
import org.emftext.sdk.concretesyntax.LineBreak;
import org.emftext.sdk.concretesyntax.PLUS;
import org.emftext.sdk.concretesyntax.Placeholder;
import org.emftext.sdk.concretesyntax.QUESTIONMARK;
import org.emftext.sdk.concretesyntax.Rule;
import org.emftext.sdk.concretesyntax.STAR;
import org.emftext.sdk.concretesyntax.Sequence;
import org.emftext.sdk.concretesyntax.SyntaxElement;
import org.emftext.sdk.concretesyntax.Terminal;
import org.emftext.sdk.concretesyntax.WhiteSpaces;
import org.emftext.sdk.util.ConcreteSyntaxUtil;
import org.emftext.sdk.util.StringUtil;

/**
 * A generator that creates a class that contains the syntax structure given
 * in the CS specification.
 */
public class GrammarInformationProviderGenerator extends JavaBaseGenerator<Object> {

	private static final String ANONYMOUS_FEATURE = "ANONYMOUS_FEATURE";

	private final static GeneratorUtil generatorUtil = new GeneratorUtil();
	private final static NameUtil nameUtil = new NameUtil();

	private ConcreteSyntax concreteSyntax;

	public GrammarInformationProviderGenerator() {
		super();
	}

	private GrammarInformationProviderGenerator(GenerationContext context) {
		super(context, TextResourceArtifacts.GRAMMAR_INFORMATION_PROVIDER);
		concreteSyntax = context.getConcreteSyntax();
	}

	@Override
	public boolean generateJavaContents(JavaComposite sc) {
		sc.add("package " + getResourcePackageName() + ";");
		sc.addLineBreak();
		sc.add("public class " + getResourceClassName() + " {");
		sc.addLineBreak();
		addStaticConstants(sc);
		addInnerClasses(sc);
		addConstantsForSyntaxElements(sc);
		
		sc.add("}");
		
		return true;
	}

	private void addConstantsForSyntaxElements(StringComposite sc) {
		Map<EObject, String> objectToFieldNameMap = new LinkedHashMap<EObject, String>();
		List<Rule> allRules = concreteSyntax.getAllRules();
		for (Rule rule : allRules) {
			addConstants(sc, objectToFieldNameMap, rule);
		}
	}

	private void addStaticConstants(StringComposite sc) {
		sc.add("public final static " + E_STRUCTURAL_FEATURE + " " + ANONYMOUS_FEATURE + " = " + ECORE_FACTORY + ".eINSTANCE.createEAttribute();");
		sc.add("static {");
		sc.add("ANONYMOUS_FEATURE.setName(\"_\");");
		sc.add("}");
		sc.addLineBreak();
	}

	private void addInnerClasses(StringComposite sc) {
		addRuleClass(sc);
	}

	private void addRuleClass(StringComposite sc) {
		sc.add("public static class Rule extends " + syntaxElementClassName + " {");
		sc.addLineBreak();
		sc.add("private final " + E_CLASS + " metaclass;"); 
		sc.addLineBreak();
		sc.add("public Rule(" + E_CLASS + " metaclass, " + choiceClassName + " choice, " + cardinalityClassName + " cardinality) {");
		sc.add("super(cardinality, new " + syntaxElementClassName + "[] {choice});"); 
		sc.add("this.metaclass = metaclass;");
		sc.add("}"); 
		sc.addLineBreak();
		sc.add("public " + E_CLASS + " getMetaclass() {"); 
		sc.add("return metaclass;"); 
		sc.add("}"); 
		sc.addLineBreak();
		sc.add("public " + choiceClassName + " getDefinition() {"); 
		sc.add("return (" + choiceClassName + ") getChildren()[0];"); 
		sc.add("}"); 
		sc.add("}"); 
		sc.addLineBreak();
	}

	private void addConstants(StringComposite sc, Map<EObject, String> objectToFieldNameMap, Rule rule) {
		addConstant(sc, objectToFieldNameMap, rule, rule.getDefinition());
		addConstant(sc, objectToFieldNameMap, rule, rule);
	}

	private void addConstant(StringComposite sc, Map<EObject, String> objectToFieldNameMap, Rule rule, EObject next) {
		if (next instanceof CsString) {
			CsString csString = (CsString) next;
			String value = csString.getValue();
			String fieldName = nameUtil.getFieldName(csString);
			sc.add("public final static " + keywordClassName + " " + fieldName + " = new " + keywordClassName + "(\"" + StringUtil.escapeToJavaString(value) + "\", " + getCardinality(next) + ");");
		} else if (next instanceof Placeholder) {
			Placeholder placeholder = (Placeholder) next;
			GenFeature genFeature = placeholder.getFeature();
			String getFeatureAccessor = getFeatureAccessor(rule.getMetaclass(), genFeature);
			String featureAccessor = getFeatureAccessor;
			String fieldName = nameUtil.getFieldName(placeholder);
			int mandatoryOccurencesAfter = getMandatoryOccurencesAfter(placeholder, genFeature);
			sc.add("public final static " + placeholderClassName + " " + fieldName + " = new " + placeholderClassName + "(" + featureAccessor + ", \"" + StringUtil.escapeToJavaString(placeholder.getToken().getName()) + "\", " + getCardinality(next) + ", " + mandatoryOccurencesAfter + ");");
		} else if (next instanceof WhiteSpaces) {
			WhiteSpaces whiteSpaces = (WhiteSpaces) next;
			int amount = whiteSpaces.getAmount();
			String fieldName = nameUtil.getFieldName(whiteSpaces);
			sc.add("public final static " + whiteSpaceClassName + " " + fieldName + " = new " + whiteSpaceClassName + "(" + amount + ", " + getCardinality(next) + ");");
		} else if (next instanceof LineBreak) {
			LineBreak lineBreak = (LineBreak) next;
			int amount = lineBreak.getTab();
			String fieldName = nameUtil.getFieldName(lineBreak);
			sc.add("public final static " + lineBreakClassName + " " + fieldName + " = new " + lineBreakClassName + "(" + getCardinality(next) + ", " + amount + ");");
		} else if (next instanceof Sequence) {
			Sequence sequence = (Sequence) next;
			List<String> elements = new ArrayList<String>();
			List<Definition> parts = sequence.getParts();
			for (Definition part : parts) {
				addConstant(sc, objectToFieldNameMap, rule, part);
				elements.add(nameUtil.getFieldName(part));
			}
			String fieldName = nameUtil.getFieldName(sequence);
			sc.add("public final static " + sequenceClassName + " " + fieldName + " = new " + sequenceClassName + "(" + getCardinality(next) + ", " + StringUtil.explode(elements, ", ") + ");");
		} else if (next instanceof Choice) {
			Choice choice = (Choice) next;
			List<String> elements = new ArrayList<String>();
			List<Sequence> parts = choice.getOptions();
			for (Sequence part : parts) {
				addConstant(sc, objectToFieldNameMap, rule, part);
				elements.add(nameUtil.getFieldName(part));
			}
			String fieldName = nameUtil.getFieldName(choice);
			sc.add("public final static " + choiceClassName + " " + fieldName + " = new " + choiceClassName + "(" + getCardinality(next) + ", " + StringUtil.explode(elements, ", ") + ");");
		} else if (next instanceof Containment) {
			Containment containment = (Containment) next;
			GenFeature genFeature = containment.getFeature();
			String featureAccessor = getFeatureAccessor(rule.getMetaclass(), genFeature);
			String fieldName = nameUtil.getFieldName(containment);
			int mandatoryOccurencesAfter = getMandatoryOccurencesAfter(containment, genFeature);
			sc.add("public final static " + containmentClassName + " " + fieldName + " = new " + containmentClassName + "(" + featureAccessor + ", " + getCardinality(next) + ", " + mandatoryOccurencesAfter + ");");
		} else if (next instanceof CompoundDefinition) {
			CompoundDefinition compound = (CompoundDefinition) next;
			Choice choice = compound.getDefinition();
			addConstant(sc, objectToFieldNameMap, rule, choice);
			String choiceFieldName = nameUtil.getFieldName(choice);
			String fieldName = nameUtil.getFieldName(compound);
			sc.add("public final static " + compoundClassName + " " + fieldName + " = new " + compoundClassName + "(" + choiceFieldName + ", " + getCardinality(next) + ");");
		} else if (next instanceof Rule) {
			Rule nextAsRule = (Rule) next;
			String definitionFieldName = nameUtil.getFieldName(nextAsRule.getDefinition());
			String metaClassAccessor = generatorUtil.getClassAccessor(nextAsRule.getMetaclass());
			String fieldName = nameUtil.getFieldName(nextAsRule);
			sc.add("public final static Rule " + fieldName + " = new Rule(" + metaClassAccessor + ", " + definitionFieldName + ", " + getCardinality(next) + ");");
		} else {
			assert next instanceof Annotation;
		}
	}

	private String getCardinality(EObject next) {
		String literal = "ONE";
		if (next instanceof CardinalityDefinition) {
			CardinalityDefinition cd = (CardinalityDefinition) next;
			Cardinality cardinality = cd.getCardinality();
			if (cardinality != null) {
				if (cardinality instanceof STAR) {
					literal = "STAR";
				} else if (cardinality instanceof PLUS) {
					literal = "PLUS";
				} else if (cardinality instanceof QUESTIONMARK) {
					literal = "QUESTIONMARK";
				}
			}
		}
		return cardinalityClassName + "." + literal;
	}

	private String getFeatureAccessor(GenClass genClass, GenFeature genFeature) {
		if (genFeature == ConcreteSyntaxUtil.ANONYMOUS_GEN_FEATURE) {
			return ANONYMOUS_FEATURE;
		} else {
			return generatorUtil.getFeatureAccessor(genClass, genFeature);
		}
	}


	private int getMandatoryOccurencesAfter(SyntaxElement syntaxElement, GenFeature feature) {
		Rule rule = syntaxElement.getContainingRule();
		int count = getMandatoryOccurencesAfter(rule, syntaxElement, feature, -1, true);
		return count < 0 ? 0 : count;
	}

	private int getMandatoryOccurencesAfter(SyntaxElement syntaxElement, SyntaxElement startAt, GenFeature feature, int count, boolean mandatory) {
		boolean isMandatory = mandatory && isMandatory(syntaxElement);
		if (syntaxElement instanceof Terminal) {
			Terminal terminal = (Terminal) syntaxElement;
			if (terminal.getFeature() == feature) {
				if (count >= 0 && isMandatory) {
					count++;
				}
			}
		}
		// check children
		for (SyntaxElement child : syntaxElement.getChildren()) {
			int childCount = getMandatoryOccurencesAfter(child, startAt, feature, count, isMandatory);
			if (childCount < 0) {
				// feature was not found yet
			} else {
				if (childCount == 0) {
					count = 0;
				} else {
					if (count < 0) {
						count = 0;
					}
					if (isMandatory) {
						count += childCount;
					}
				}
			}
			if (syntaxElement instanceof Choice) {
				break;
			}
		}
		if (startAt == syntaxElement) {
			count = 0;
		}
		return count;
	}

	private boolean isMandatory(SyntaxElement element) {
		if (element instanceof CardinalityDefinition) {
			CardinalityDefinition cd = (CardinalityDefinition) element;
			Cardinality cardinality = cd.getCardinality();
			if (cardinality instanceof STAR || cardinality instanceof QUESTIONMARK) {
				return false;
			}
		}
		return true;
	}

	public IGenerator<GenerationContext, Object> newInstance(GenerationContext context, Object parameters) {
		return new GrammarInformationProviderGenerator(context);
	}
}
