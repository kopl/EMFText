@SuppressWarnings(featureWithoutSyntax,noRuleForMetaClass)
SYNTAXDEF cs 
FOR       <http://www.emftext.org/sdk/concretesyntax> <concretesyntax.genmodel>
START     ConcreteSyntax

OPTIONS {	
	licenceHeader ="../../org.dropsbox/licence.txt";
	// token options
	tokenspace = "0";
	defaultTokenName = "QUALIFIED_NAME";
	usePredefinedTokens = "false";
	
	// override options
	overrideManifest = "false";
	overrideHoverTextProvider = "false";
	overrideProposalPostProcessor = "false";
	overrideResourcePostProcessor = "false";
	overrideOutlinePageActionProvider = "false";
	overrideUIPluginActivator = "false";
	
	overrideNewFileContentProvider = "false";
	overrideNewFileWizard = "false";
	overrideNewProjectWizardLogic = "false";
	
	// adjust dependencies
	additionalUIDependencies = "org.emftext.sdk";
	antlrPluginID = "org.emftext.sdk";
	overrideAntlrPlugin = "false";

	// disable stuff we don't need	
	disableBuilder = "true";
	// we need to disable the use of the EMF validation framework, because
	// it does not run outside of Eclipse properly, which conflicts with
	// the EMFText ANT task 
	disableEMFValidationConstraints = "true";
	// we also need to disable the EValidators since they do cause problems
	// as well 
	disableEValidators = "true";
	disableLaunchSupport = "true";
	disableDebugSupport = "true";
	disableNewProjectWizard = "true";

	resolveProxyElementsAfterParsing = "false";
	EMFTargetVersion = "2.9";
}

TOKENS {
	DEFINE SL_COMMENT $'//'(~('\n'|'\r'|'\uffff'))*$;
	DEFINE ML_COMMENT $'/*'.*'*/'$;
	
	DEFINE QUALIFIED_NAME $('A'..'Z'|'a'..'z'|'_')('A'..'Z'|'a'..'z'|'_'|'-'|'0'..'9')*('.'('A'..'Z'|'a'..'z'|'_'|'-'|'0'..'9')+)*$;
	DEFINE FRAGMENT NUMBER $('0'..'9')+$;
	DEFINE HEXNUMBER $'#'('0'..'9'|'A'..'F'|'a'..'f')+$;
	DEFINE TABNUMBER $'!'$ + NUMBER;
	DEFINE STRING $'"'('\\'('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')|('\\''u'('0'..'9'|'a'..'f'|'A'..'F')('0'..'9'|'a'..'f'|'A'..'F')('0'..'9'|'a'..'f'|'A'..'F')('0'..'9'|'a'..'f'|'A'..'F'))|'\\'('0'..'7')|~('\\'|'"'))*'"'$;
	DEFINE WHITESPACE $(' '|'\t'|'\f')$;
	DEFINE LINEBREAK $('\r\n'|'\r'|'\n')$;
}

TOKENSTYLES {
	// numbers
	"HEXNUMBER", "TABNUMBER" COLOR #00D0FF;
	
	// keywords related to tokens and token styles 
	"DEFINE", "REDEFINE", "AS", "FRAGMENT", "COLLECT", "IN",
	"COLOR", "PRIORITIZE" COLOR #FF9000, BOLD;
	
	// comments
	"SL_COMMENT", "ML_COMMENT" COLOR #008000;

	// other keywords
	"ABSTRACT", "SYNTAXDEF", "FOR", "START", "IMPORTS",
	"OPTIONS", "TOKENS", "TOKENSTYLES", "RULES" COLOR #800040, BOLD;
	
	// strings
	"STRING", "QUOTED_39_39_92" COLOR #2A00FF;
	
	// URIs
	"QUOTED_60_62" COLOR #000000;
}

RULES {

	ConcreteSyntax ::= 
		(annotations !0)*
		abstract["ABSTRACT" : ""]
		"SYNTAXDEF" #1 name[] !0 
		"FOR" #1 package['<','>'] (#1 packageLocationHint['<','>'])? !0 
		("START" #1 (startSymbols[]) ("," (startSymbols[]))*)? 
		(!0 !0 "IMPORTS" #1 "{" ( !1 imports)* !0 "}")? 
		(!0 !0 "OPTIONS" #1 "{" (!1 options ";" )*  !0 "}")? 
		(!0 !0 "TOKENS" #1 "{" ( !1 tokens ";")* !0 "}")? 
		(!0 !0 "TOKENSTYLES" #1 "{" ( !1 tokenStyles)* !0 "}")? 
		 !0 !0 "RULES" #1 "{" (!1 rules)* !0 "}"
		;

	Import         ::= 
		(annotations !0)*
		prefix[] ":" package['<','>'] (#1 packageLocationHint['<','>'])? ( #1 "WITH" #1 "SYNTAX" #1 concreteSyntax[] (#1 csLocationHint['<','>'])?)?;
 
	Option 	       ::= type[] #1 "=" #1 value[STRING];
 
	@Foldable
	Rule           ::= (annotations !0)* metaclass[] #1 "::=" #1 children : Choice ";";
 
	Sequence       ::= children : Definition (#1 children : Definition)*;
 
	Choice         ::= children : Sequence (#1 "|" #1 children : Sequence)*;	

	CsString       ::= value[STRING];
	
	PlaceholderUsingSpecifiedToken ::= feature[] "[" token[] "]" 
	                                   cardinality[none : "", plus : "+", star : "*", questionmark : "?"];
	PlaceholderUsingDefaultToken   ::= feature[] "[" "]" 
	                                   cardinality[none : "", plus : "+", star : "*", questionmark : "?"];
	PlaceholderInQuotes            ::= feature[] "[" prefix['\'','\'','\\'] "," suffix['\'','\'','\\'] ("," escapeCharacter['\'','\'','\\'])? "]"
	                                   cardinality[none : "", plus : "+", star : "*", questionmark : "?"];
	BooleanTerminal                ::= feature[] "[" trueLiteral[STRING] #1 ":" #1 falseLiteral[STRING] "]" 
	                                   cardinality[none : "", plus : "+", star : "*", questionmark : "?"];
	EnumTerminal                   ::= feature[] "[" literals ("," #1 literals)* "]" 
	                                   cardinality[none : "", plus : "+", star : "*", questionmark : "?"];
	EnumLiteralTerminal            ::= literal[] ":" text[STRING];
	
	Containment                    ::= feature[] (":" types[] ("," types[])*)? 
							           cardinality[none : "", plus : "+", star : "*", questionmark : "?"]
							           ;
	
	CompoundDefinition             ::= "(" children : Choice ")" 
	                                   cardinality[none : "", plus : "+", star : "*", questionmark : "?"];

	WhiteSpaces  ::= amount[HEXNUMBER];
	LineBreak    ::= tab[TABNUMBER];
	
	TokenRedefinition      ::= (annotations !0)* "REDEFINE" #1 redefinedToken[] "AS" name[] #1 regexParts (#1 "+" #1 regexParts)*;
	NormalTokenDefinition  ::= (annotations !0)* "DEFINE" #1 name[] #1 regexParts (#1 "+" #1 regexParts)* (#1 "COLLECT" #1 "IN" #1 attributeName[])?;
	PartialTokenDefinition ::= "DEFINE" #1 "FRAGMENT" #1 name[] #1 regexParts (#1 "+" #1 regexParts)*;
	TokenPriorityDirective ::= "PRIORITIZE" #1 token[];
	
	AtomicRegex    ::= atomicExpression['$','$'];
	RegexReference ::= target[];

	TokenStyle ::= tokenNames[STRING] ("," #1 tokenNames[STRING])* #1 "COLOR" #1 rgb[HEXNUMBER] ("," #1 fontStyles[])* ";";
	
	Annotation ::= "@" type[] ("(" parameters ("," parameters)* ")")?;
	
	KeyValuePair ::= key[] ("=" value[STRING])?;
}