editAreaLoader.load_syntax["groovy"] = {
	'DISPLAY_NAME' : 'Groovy'
	,'COMMENT_SINGLE': { 1: '//', 2: '@' }
	, 'COMMENT_MULTI': { '/*': '*/' }
	, 'QUOTEMARKS': { 1: "'", 2: '"' }
	, 'KEYWORD_CASE_SENSITIVE': true
	, 'KEYWORDS': {
	    'constants': [
			'null', 'false', 'true'
		]
		, 'types': [
			'String', 'int', 'short', 'long', 'char', 'double', 'byte',
			'float', 'static', 'void', 'private', 'boolean', 'protected',
			'public', 'const', 'class', 'final', 'abstract', 'volatile',
			'enum', 'transient', 'interface'
		]
		, 'statements': [
            'this', 'extends', 'if', 'do', 'while', 'try', 'catch', 'finally',
            'throw', 'throws', 'else', 'for', 'switch', 'continue', 'implements',
            'break', 'case', 'default', 'goto'
		]
 		, 'keywords': [
           'as', 'assert', 'break', 'case', 'catch', 'class', 'continue', 'def',
           'default', 'do', 'else', 'extends', 'finally', 'if', 'in',
           'implements', 'import', 'instanceof', 'interface', 'new', 'package',
           'property', 'return', 'switch', 'throw', 'throws', 'try', 'while',
           'public', 'protected', 'private', 'static'
		]
    , 'methods' : [
           'allProperties', 'count', 'get', 'size', 'collect', 'each',
           'eachProperty', 'eachPropertyName', 'eachWithIndex', 'find',
           'findAll', 'findIndexOf', 'grep', 'inject', 'max', 'min',
           'reverseEach', 'sort', 'asImmutable', 'asSynchronized', 'flatten',
           'intersect', 'join', 'pop', 'reverse', 'subMap', 'toList',
           'padRight', 'padLeft', 'contains', 'eachMatch', 'toCharacter',
           'toLong', 'toUrl', 'tokenize', 'eachFile', 'eachFileRecurse',
           'eachByte', 'eachLine', 'readBytes', 'readLine', 'getText',
					 'splitEachLine', 'withReader', 'append', 'encodeBase64',
           'decodeBase64', 'filterLine', 'transformChar', 'transformLine',
           'withOutputStream', 'withPrintWriter', 'withStream', 'withStreams',
           'withWriter', 'withWriterAppend', 'write', 'writeLine', 'dump',
           'inspect', 'invokeMethod', 'print', 'println', 'step', 'times',
           'upto', 'use', 'waitForOrKill', 'getText'
    ]
	}
	, 'OPERATORS': [
		'+', '-', '/', '*', '=', '<', '>', '%', '!', '?', ':', '&'
	]
	, 'DELIMITERS': [
		'(', ')', '[', ']', '{', '}'
	]
	, 'REGEXPS': {
	    'precompiler': {
	        'search': '()(#[^\r\n]*)()'
			, 'class': 'precompiler'
			, 'modifiers': 'g'
			, 'execute': 'before'
	    }
	}
	, 'STYLES': {
	    'COMMENTS': 'color: #AAAAAA;'
		, 'QUOTESMARKS': 'color: #6381F8;'
		, 'KEYWORDS': {
		    'constants': 'color: #EE0000;'
			, 'types': 'color: #0000EE;'
			, 'statements': 'color: #60CA00;'
			, 'keywords': 'color: #48BDDF;'
      , 'methods': 'color: #48BDDF;'
		}
		, 'OPERATORS': 'color: #FF00FF;'
		, 'DELIMITERS': 'color: #0038E1;'
		, 'REGEXPS': {
		    'precompiler': 'color: #009900;'
			, 'precompilerstring': 'color: #994400;'
		}
	}
};
