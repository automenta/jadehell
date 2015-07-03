/* Generated By:JavaCC: Do not edit this line. ExtendedSLParser.java */
package jade.content.lang.sl;

import jade.content.abs.*;
import jade.content.onto.Ontology;
import jade.core.CaseInsensitiveString;
import jade.content.lang.Codec;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Date;

import org.apache.commons.codec.binary.Base64;


/**
* ExtendedSLParser. This same parser embeds also lower profiles of SL, namely SL-0, SL-1 and SL-2.
* @author Fabio Bellifemine, TILab S.p.A. (formerly CSELT S.p.A.)
* @author Nicolas Lhuillier (Motorola) (added support for PREFIXBYTELENGTHENCODEDSTRING)
* @version $Date: 2005-05-13 18:14:10 +0200 (Fri, 13 May 2005) $ $Revision: 5696 $
**/
class ExtendedSLParser implements ExtendedSLParserConstants {

  private static final String META_EXCEPTION_MESSAGE = "Meta SL expressions are not allowed";
                /** This variable is true, when meta symbols are allowed (metas are a semantics-specific extension to the SL Grammar) **/
                private boolean metaAllowed = true; //FIXME to do set/unset this variable 

     /* Take a quoted FIPA SL0 String and convert to a 
      * normal Java-style String.  Remove the
      * leading/trailing quotation marks, and
      * un-escape any included quotation marks.
      * This must be the exact inverse of the 
      * escape() procedure in the SLEncoder.
     */
     private String unescape(String s) {
          StringBuffer result = new StringBuffer(s.length());
          for( int i=1; i<s.length()-1; i++)
                if( s.charAt(i) == '\u005c\u005c' && s.charAt(i+1) == '\u005c"' ) {
                  result.append("\u005c"");
                  i++;
                } else
                  result.append(s.charAt(i));
                return result.toString();
     }


     /**
     * When an ActionExpression is parsed, if it is an AbsConcept then
     * it must be casted upto an AbsAgentAction. 
     **/
     private AbsTerm toAbsAgentAction(AbsTerm t) {
        if ((t instanceof AbsConcept) && (!(t instanceof AbsAgentAction))) {
                AbsAgentAction act = new AbsAgentAction(t.getTypeName());
                String[] slotNames = t.getNames();
                if (slotNames != null) {
                        for (int i=0; i<slotNames.length; i++)
                                act.set(slotNames[i], (AbsTerm) t.getAbsObject(slotNames[i]));
                }
                return act;
        } else
                return t;
     }

  /**
   * By default an object of this type implements a Full ExtendedSLParser.
   * This method allows to change this default.
   * @param slType (0 for FIPa-SL0, 1 for SL1, 2 for SL2, >2 for full SL) 
  **/
  void setSLType(int slType) {
     this.slType = slType;
  }


  Ontology curOntology = null;
  /**
   * Reinitialize the parser such as it is ready to parse a new expression.
   * @param content the content to be parsed
   * @param o the ontology, null if no ontology (this parameter is used to get the names of the slots
   * when they are encoded as unnamed slots.
  */
  void reinit(Ontology o, String content) {
    curOntology = o;
    if (content == null) content = new String();
    ReInit(new StringReader(content));
  }

  /**
   * @param content the content to be parsed
   * @param o the ontology, null if no ontology (this parameter is used to get the names of the slots
   * when they are encoded as unnamed slots.
   * @deprecated since JADE 3.4 it is preferrable to use reinit() and then call directly the method corresponding to the production rule (e.g. Content())
  */
  AbsContentElement parse(Ontology o, String content) throws ParseException, TokenMgrError{
          reinit(o, content);
    AbsContentElementList tuple = Content();
    if (tuple.size() > 1)
      return tuple;
    else  // if there is a single ContentExpression than return just it, not the tuple
      return tuple.get(0);
  }

  /** (0 for FIPa-SL0, 1 for SL1, 2 for SL2, >2 for full SL) **/
  int slType=3;

  public static void main(String[] args) {

    ExtendedSLParser theParser = null;
    try {
      theParser = new ExtendedSLParser(System.in);
      theParser.setSLType(Integer.parseInt(args[0]));
    } catch (Exception e) {
      System.out.println("usage: ExtendedSLParser SLLevel\u005cn  where SLLevel can be 0 for SL0, 1 for SL1, 2 for SL2, 3 or more for full SL");
      System.exit(0);
    }
    if (theParser.slType < 3)
       System.out.println("SL-"+theParser.slType+" Parser Started ...");
    else
       System.out.println("Full-SL"+" Parser Started ...");

    SLCodec codec = new SLCodec();
    //Ontology o = new DefaultOntology();

    while (true) {
        System.out.println("insert an SL expression to parse: ");
      try {
        AbsContentElementList result = theParser.Content();
  String resultEncoded = codec.encode(result);
        System.out.println("\u005cn\u005cn RESULT of ExtendedSLParser.Content()=\u005cn"+resultEncoded);
  AbsContentElement result2 = codec.decode(resultEncoded);
        System.out.println("\u005cn\u005cn RESULT of SLCodec.decode(SLCodec.encode(ExtendedSLParser.Content()))=\u005cn"+codec.encode(result2));
        System.out.println("\u005cn\u005cn");
        //result.dump();
        //System.out.println("AFTER ENCODING: "+codec.encode(result,o));
      }
      catch(Exception pe) {
        pe.printStackTrace();
        System.exit(0);
      }
    }
  }

/*   P R O D U C T I O N    R U L E S  */

/**
* This production rule represents the more general expression that can
* serve as content for an ACL message. Since different communicative
* acts have different content (action expressions for
* <code>request</code>, predicate for <code>inform</code>, etc.), any
* allowed SL content expression can be parsed from here.
*/
  final public AbsContentElementList Content() throws ParseException {
  AbsContentElementList tuple = new AbsContentElementList();
  AbsContentElement     val;
    LBrace();
    label_1:
    while (true) {
      val = ContentExpression();
                                    tuple.add(val);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
      case WORD:
      case STRING_LITERAL:
      case METASYMBOL:
      case LBRACE2:
      case WORD2:
      case STRING_LITERAL2:
      case METASYMBOL2:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    RBrace();
  {if (true) return tuple;}
    throw new Error("Missing return statement in function");
  }

/** Left Brace in all of the possible states of the Token Manager **/
  final public void LBrace() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
      jj_consume_token(LBRACE);
      break;
    case LBRACE2:
      jj_consume_token(LBRACE2);
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

                         // lbrace2 in the OperatorState of the Token Manager

/** Right Brace in all of the possible states of the Token Manager **/
  final public void RBrace() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case RBRACE:
      jj_consume_token(RBRACE);
      break;
    case RBRACE2:
      jj_consume_token(RBRACE2);
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

                         // rbrace2 in the OperatorState of the Token Manager
  final public AbsContentElement ContentExpression() throws ParseException {
  AbsContentElement val=null; String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WORD:
    case STRING_LITERAL:
    case WORD2:
    case STRING_LITERAL2:
      s = String();
                      val=new AbsPredicate(s);
      break;
    case LBRACE:
    case LBRACE2:
      LBrace();
      val = ContentExpression_NoBrace();
      RBrace();
      break;
    case METASYMBOL:
    case METASYMBOL2:
      s = MetaSymbol();
                      AbsPredicate val1=new AbsPredicate(s); val1.setIsMetaFormula(true); val=val1;
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  val.setIsAContentExpression(true); {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsContentElement ContentExpression_NoBrace() throws ParseException {
  AbsContentElement val=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case REFERENTIALOP:
      val = IdentifyingExpression_NoBrace();
      break;
    case ACTION:
    case ACTIONOPLL:
      val = ActionExpression_NoBrace();
      break;
    case WORD:
    case STRING_LITERAL:
    case MODALOP:
    case ACTIONOP:
    case UNARYLOGICALOP:
    case BINARYLOGICALOP:
    case QUANTIFIER:
    case WORD2:
    case STRING_LITERAL2:
      val = Wff_NoBrace();
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsIRE IdentifyingExpression_NoBrace() throws ParseException {
  Token t; AbsIRE ire=null; AbsPredicate prop; AbsTerm term; AbsVariable var;
    t = jj_consume_token(REFERENTIALOP);
                    if (slType<2) {if (true) throw new ParseException("NotFullSL_IdentifyExpression_NotParsable_UseAtLeastSL2");}
    term = Term();
    /*var=Variable()*/ prop = Wff();
   ire = new AbsIRE(t.image);
   /*ire.setVariable(var);*/
   ire.setTerm(term);
   ire.setProposition(prop);
   {if (true) return ire;}
    throw new Error("Missing return statement in function");
  }

  final public AbsVariable Variable() throws ParseException {
  AbsVariable val=null; Token v;
    v = jj_consume_token(VARIABLE);
   val = new AbsVariable(); val.setName(v.image.substring(1)); {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsTerm Term() throws ParseException {
  Token v; AbsTerm val=null; String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VARIABLE:
      val = Variable();
      break;
    case INTEGER:
    case HEXINTEGER:
    case LONG:
    case FLOATONE:
    case FLOATTWO:
    case FLOAT:
    case WORD:
    case STRING_LITERAL:
    case DATETIME:
    case PREFIXBYTELENGTHENCODEDSTRING:
    case WORD2:
    case STRING_LITERAL2:
      val = Constant();
      break;
    case LBRACE:
    case LBRACE2:
      LBrace();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case WORD:
      case STRING_LITERAL:
      case ARITHMETICOP:
      case WORD2:
      case STRING_LITERAL2:
        val = FunctionalTerm_NoBrace();
        break;
      case ACTION:
      case ACTIONOPLL:
        val = ActionExpression_NoBrace();
        break;
      case REFERENTIALOP:
        val = IdentifyingExpression_NoBrace();
        break;
      default:
        jj_la1[5] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      RBrace();
      break;
    case METASYMBOL:
    case METASYMBOL2:
      s = MetaSymbol();
                     AbsVariable val1=new AbsVariable(); val1.setName(s); val1.setIsMetaTerm(true); val=val1;
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsPrimitive Constant() throws ParseException {
  String s; AbsPrimitive val=null; Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WORD:
    case STRING_LITERAL:
    case WORD2:
    case STRING_LITERAL2:
      s = String();
        // if it is true or false, then converts the String into a Boolean
        if (CaseInsensitiveString.equalsIgnoreCase(s, "true"))
                val = AbsPrimitive.wrap(true);
        else if (CaseInsensitiveString.equalsIgnoreCase(s, "false"))
                val = AbsPrimitive.wrap(false);
        else {
        if (  (CaseInsensitiveString.equalsIgnoreCase(s,"\u005c"true\u005c""))
            ||(CaseInsensitiveString.equalsIgnoreCase(s,"\u005c"false\u005c"")) )
                  // in this case leading/trailing quotes were added by the
                  // encoder and now they must be removed. 
                  s = unescape(s);
                    val = AbsPrimitive.wrap(s);
        }
      break;
    case INTEGER:
    case HEXINTEGER:
    case LONG:
    case FLOATONE:
    case FLOATTWO:
    case FLOAT:
      val = Number();
      break;
    case DATETIME:
      t = jj_consume_token(DATETIME);
    try {
      Date d=jade.lang.acl.ISO8601.toDate(t.image);
      val = AbsPrimitive.wrap(d);
    } catch (Exception e) {
      val = AbsPrimitive.wrap(t.image);
                 }
      break;
    case PREFIXBYTELENGTHENCODEDSTRING:
      t = jj_consume_token(PREFIXBYTELENGTHENCODEDSTRING);
        try {
                byte[]byteArray = Base64.decodeBase64(t.image.getBytes("US-ASCII"));
                val = AbsPrimitive.wrap(byteArray);
        } catch (UnsupportedEncodingException uee) {
                {if (true) throw new ParseException("Error decoding byte-array from Base64 US-ASCII, "+uee.getMessage());}
        }
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsConcept FunctionalTerm_NoBrace() throws ParseException {
  Token t; AbsTerm term1, term2; AbsConcept val=null; String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ARITHMETICOP:
      t = jj_consume_token(ARITHMETICOP);
      term1 = Term();
      term2 = Term();
        val = new AbsConcept(t.image);
        try {
           String slotNames[] = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], term1);
           val.set(slotNames[1], term2);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", term1);
           val.set(Codec.UNNAMEDPREFIX+"1", term2);
        }
      break;
    case WORD:
    case STRING_LITERAL:
    case WORD2:
    case STRING_LITERAL2:
      s = String();
      if ( (SL0Vocabulary.SET.equalsIgnoreCase(s)) || (SL0Vocabulary.SEQUENCE.equalsIgnoreCase(s)))
        val = new AbsAggregate(s);
      else {
        try {
          val = (AbsConcept) curOntology.getSchema(s).newInstance();
        }
        catch (Exception e) {
          val = new AbsConcept(s);
        }
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
      case VARIABLE:
      case INTEGER:
      case HEXINTEGER:
      case LONG:
      case FLOATONE:
      case FLOATTWO:
      case FLOAT:
      case WORD:
      case STRING_LITERAL:
      case PARAMETERNAME:
      case PARAMETERNAME_STRING:
      case DATETIME:
      case PREFIXBYTELENGTHENCODEDSTRING:
      case METASYMBOL:
      case LBRACE2:
      case WORD2:
      case STRING_LITERAL2:
      case METASYMBOL2:
        FunctionalTermParameters(val);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

// artificial production, needed to avoid a bad warning from javacc
// val can be either an AbsConcept or an AbsAggregate
  final public void FunctionalTermParameters(AbsConcept val) throws ParseException {
  AbsTerm t; int slotNumber=0;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
    case VARIABLE:
    case INTEGER:
    case HEXINTEGER:
    case LONG:
    case FLOATONE:
    case FLOATTWO:
    case FLOAT:
    case WORD:
    case STRING_LITERAL:
    case DATETIME:
    case PREFIXBYTELENGTHENCODEDSTRING:
    case METASYMBOL:
    case LBRACE2:
    case WORD2:
    case STRING_LITERAL2:
    case METASYMBOL2:
       String slotNames[] = null;
         try {
           slotNames = curOntology.getSchema(val.getTypeName()).getNames();
         } catch (Exception e) {
         }
      label_2:
      while (true) {
        t = Term();
         try {
               val.set(slotNames[slotNumber], t);
         } catch (Exception e) {
               val.set(Codec.UNNAMEDPREFIX+slotNumber,t);
         }
        slotNumber++;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACE:
        case VARIABLE:
        case INTEGER:
        case HEXINTEGER:
        case LONG:
        case FLOATONE:
        case FLOATTWO:
        case FLOAT:
        case WORD:
        case STRING_LITERAL:
        case DATETIME:
        case PREFIXBYTELENGTHENCODEDSTRING:
        case METASYMBOL:
        case LBRACE2:
        case WORD2:
        case STRING_LITERAL2:
        case METASYMBOL2:
          ;
          break;
        default:
          jj_la1[10] = jj_gen;
          break label_2;
        }
      }
      break;
    case PARAMETERNAME:
    case PARAMETERNAME_STRING:
      label_3:
      while (true) {
        Parameter(val);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PARAMETERNAME:
        case PARAMETERNAME_STRING:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_3;
        }
      }
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void Parameter(AbsConcept val) throws ParseException {
  Token t; AbsTerm term; String slotName;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PARAMETERNAME:
      t = jj_consume_token(PARAMETERNAME);
                               slotName = t.image.substring(1);
      break;
    case PARAMETERNAME_STRING:
      t = jj_consume_token(PARAMETERNAME_STRING);
                               slotName = unescape(t.image.substring(1));
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    term = Term();
                               val.set(slotName,term);
  }

  final public AbsAgentAction ActionExpression_NoBrace() throws ParseException {
  AbsAgentAction val=null; AbsTerm term1, term2; Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ACTION:
      t = jj_consume_token(ACTION);
      term1 = Term();
      term2 = Term();
       val = new AbsAgentAction(t.image);
       try {
           String slotNames[] = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], term1);
           val.set(slotNames[1], toAbsAgentAction(term2));
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", term1);
           val.set(Codec.UNNAMEDPREFIX+"1", toAbsAgentAction(term2));
        }
      break;
    case ACTIONOPLL:
      t = jj_consume_token(ACTIONOPLL);
                    if (slType<2) {if (true) throw new ParseException("NotFullSL_ActionOperatorExpression_NotParsable");}
      term1 = ActionExpression();
      term2 = ActionExpression();
       val = new AbsAgentAction(t.image);
       try {
           String slotNames[] = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], term1);
           val.set(slotNames[1], toAbsAgentAction(term2));
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", term1);
           val.set(Codec.UNNAMEDPREFIX+"1", toAbsAgentAction(term2));
        }
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsTerm ActionExpression() throws ParseException {
  AbsTerm val=null; String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRACE:
    case LBRACE2:
      LBrace();
      val = ActionExpression_NoBrace();
      RBrace();
      break;
    case METASYMBOL:
    case METASYMBOL2:
      s = MetaSymbol();
                     AbsVariable val1=new AbsVariable(); val1.setName(s); val1.setIsMetaTerm(true); val=val1;
      break;
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
   {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsPredicate Wff() throws ParseException {
  AbsPredicate val=null; String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WORD:
    case STRING_LITERAL:
    case WORD2:
    case STRING_LITERAL2:
      s = String();
                 val = new AbsPredicate(s);
      break;
    case LBRACE:
    case LBRACE2:
      LBrace();
      val = Wff_NoBrace();
      RBrace();
      break;
    case METASYMBOL:
    case METASYMBOL2:
      s = MetaSymbol();
                     val=new AbsPredicate(s); val.setIsMetaFormula(true);
      break;
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsPredicate Wff_NoBrace() throws ParseException {
  AbsPredicate arg1, arg2, val=null; Token t; AbsTerm term; String s;
  String slotNames[]=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case UNARYLOGICALOP:
      t = jj_consume_token(UNARYLOGICALOP);
                        if (slType<1) {if (true) throw new ParseException("NotFullSL_NotExpression_NotParsable_UseAtLeastSL1");}
      arg1 = Wff();
    val = new AbsPredicate(t.image);
    try {
           slotNames = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], arg1);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", arg1);
        }
      break;
    case BINARYLOGICALOP:
      t = jj_consume_token(BINARYLOGICALOP);
                         if (slType<1) {if (true) throw new ParseException("NotFullSL_BinaryLogicalExpression_NotParsable_UseAtLeastSL1");}
      arg1 = Wff();
      arg2 = Wff();
    val = new AbsPredicate(t.image);
    try {
           slotNames = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], arg1);
           val.set(slotNames[1], arg2);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", arg1);
           val.set(Codec.UNNAMEDPREFIX+"1", arg2);
        }
      break;
    case QUANTIFIER:
      t = jj_consume_token(QUANTIFIER);
                    if (slType<2) {if (true) throw new ParseException("NotFullSL_QuantifierExpression_NotParsable_UseAtLeastSL2");} AbsVariable var;
      var = Variable();
      arg1 = Wff();
    val = new AbsPredicate(t.image);
    try {
           slotNames = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], var);
           val.set(slotNames[1], arg1);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", var);
           val.set(Codec.UNNAMEDPREFIX+"1", arg1);
        }
      break;
    case MODALOP:
      t = jj_consume_token(MODALOP);
                 if (slType<2) {if (true) throw new ParseException("NotFullSL_ModalOperatorExpression_NotParsable_UseAtLeastSL2");}
      term = Term();
      arg1 = Wff();
    val = new AbsPredicate(t.image);
    try {
           slotNames = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], term);
           val.set(slotNames[1], arg1);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", term);
           val.set(Codec.UNNAMEDPREFIX+"1", arg1);
        }
      break;
    case ACTIONOP:
      t = jj_consume_token(ACTIONOP);
      term = ActionExpression();
    val = new AbsPredicate(t.image);
    try {
           slotNames = curOntology.getSchema(t.image).getNames();
           val.set(slotNames[0], term);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"0", term);
        }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRACE:
      case WORD:
      case STRING_LITERAL:
      case METASYMBOL:
      case LBRACE2:
      case WORD2:
      case STRING_LITERAL2:
      case METASYMBOL2:
        arg1 = Wff();
    try {
           val.set(slotNames[1], arg1);
        } catch (Exception e) {
           val.set(Codec.UNNAMEDPREFIX+"1", arg1);
        }
        break;
      default:
        jj_la1[17] = jj_gen;
        ;
      }
      break;
    case WORD:
    case STRING_LITERAL:
    case WORD2:
    case STRING_LITERAL2:
      s = String();
                val = new AbsPredicate(s); int slotNumber=0;
                try {
                   slotNames = curOntology.getSchema(s).getNames();
                } catch (Exception e) {
                }
      label_4:
      while (true) {
        term = Term();
                try {
                  val.set(slotNames[slotNumber], term);
              } catch (Exception e) {
                  val.set(Codec.UNNAMEDPREFIX+slotNumber, term);
              }
              slotNumber++;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case LBRACE:
        case VARIABLE:
        case INTEGER:
        case HEXINTEGER:
        case LONG:
        case FLOATONE:
        case FLOATTWO:
        case FLOAT:
        case WORD:
        case STRING_LITERAL:
        case DATETIME:
        case PREFIXBYTELENGTHENCODEDSTRING:
        case METASYMBOL:
        case LBRACE2:
        case WORD2:
        case STRING_LITERAL2:
        case METASYMBOL2:
          ;
          break;
        default:
          jj_la1[18] = jj_gen;
          break label_4;
        }
      }
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

  final public AbsPrimitive Number() throws ParseException {
   Token t; AbsPrimitive val = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER:
      t = jj_consume_token(INTEGER);
                  try {
                   val = AbsPrimitive.wrap(Integer.parseInt(t.image));
                  } catch (NumberFormatException e) { //should never happen
                   e.printStackTrace();
                   val=AbsPrimitive.wrap(t.image);
                  }
      break;
    case HEXINTEGER:
      t = jj_consume_token(HEXINTEGER);
                      val=AbsPrimitive.wrap(t.image);
      break;
    case LONG:
      t = jj_consume_token(LONG);
                String longStr;
                  try {
                   longStr = t.image.substring(0, t.image.length() - 1);
                   val = AbsPrimitive.wrap(Long.parseLong(longStr));
                  } catch (NumberFormatException e) { //should never happen
                    e.printStackTrace();
                    val=AbsPrimitive.wrap(t.image);
                  }
      break;
    case FLOATONE:
      t = jj_consume_token(FLOATONE);
                    double d1;
                   try {
                    // J2ME incompatible d1=Double.parseDouble(t.image); 
                    d1=(new Double(t.image)).doubleValue();
                    val=AbsPrimitive.wrap(d1);
                  } catch (NumberFormatException e) { //should never happen
                    e.printStackTrace();
                    val=AbsPrimitive.wrap(t.image);
                  }
      break;
    case FLOATTWO:
      t = jj_consume_token(FLOATTWO);
                    double d2;
                   try {
                    // J2ME incompatible d2=Double.parseDouble(t.image); 
                    d2=(new Double(t.image)).doubleValue();
                    val=AbsPrimitive.wrap(d2);
                  } catch (NumberFormatException e) { //should never happen
                    e.printStackTrace();
                    val=AbsPrimitive.wrap(t.image);
                  }
      break;
    case FLOAT:
      t = jj_consume_token(FLOAT);
                 String floatStr;
                  try {
                   floatStr = t.image.substring(0, t.image.length() - 1);
                   val = AbsPrimitive.wrap((new Float(floatStr)).floatValue());
                  } catch (NumberFormatException e) { //should never happen
                    e.printStackTrace();
                    val=AbsPrimitive.wrap(t.image);
                  }
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
 {if (true) return val;}
    throw new Error("Missing return statement in function");
  }

/**
* <p> <code> String = WORD | STRING_LITERAL  </code>
*/
  final public String String() throws ParseException {
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WORD2:
      t = jj_consume_token(WORD2);
                         {if (true) return t.image;}
      break;
    case STRING_LITERAL2:
      t = jj_consume_token(STRING_LITERAL2);
                         if (  (CaseInsensitiveString.equalsIgnoreCase(t.image,"\u005c"true\u005c""))
                             ||(CaseInsensitiveString.equalsIgnoreCase(t.image,"\u005c"false\u005c"")) )
                            // in this case leading/trailing quotes must be left
                            // otherwise the value is confused with a boolean
                            {if (true) return t.image;}
                         else
                            {if (true) return unescape(t.image);}
      break;
    case WORD:
      t = jj_consume_token(WORD);
                         {if (true) return t.image;}
      break;
    case STRING_LITERAL:
      t = jj_consume_token(STRING_LITERAL);
                         if (  (CaseInsensitiveString.equalsIgnoreCase(t.image,"\u005c"true\u005c""))
                             ||(CaseInsensitiveString.equalsIgnoreCase(t.image,"\u005c"false\u005c"")) )
                            // in this case leading/trailing quotes must be left
                            // otherwise the value is confused with a boolean
                            {if (true) return t.image;}
                         else
                            {if (true) return unescape(t.image);}
      break;
    default:
      jj_la1[21] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

/** This grammar rule is an extension to the FIPA SL Grammar. It is specific to the semantics framework.
    It allows to represent a symbol which is not matched by any FIPA-SL token. **/
  final public String MetaSymbol() throws ParseException {
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case METASYMBOL:
      t = jj_consume_token(METASYMBOL);
      break;
    case METASYMBOL2:
      t = jj_consume_token(METASYMBOL2);
      break;
    default:
      jj_la1[22] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
       if (!metaAllowed) {if (true) throw new ParseException(META_EXCEPTION_MESSAGE);} else {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public ExtendedSLParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[23];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x210c020,0x2000020,0x4000040,0x210c020,0xb800c000,0x5800c000,0x21cffa0,0xcff00,0x21fffa0,0x4000c000,0x21cffa0,0x30000,0x21fffa0,0x30000,0x8000000,0x2100020,0x210c020,0x210c020,0x21cffa0,0xa000c000,0x3f00,0xc000,0x100000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x70,0x0,0x0,0x70,0x3f,0x31,0x70,0x30,0x70,0x30,0x70,0x0,0x70,0x0,0x1,0x40,0x70,0x70,0x70,0x3e,0x0,0x30,0x40,};
   }

  /** Constructor with InputStream. */
  public ExtendedSLParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public ExtendedSLParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    token_source = new ExtendedSLParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(UnsupportedEncodingException e) { throw new RuntimeException(e.getMessage(), e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public ExtendedSLParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ExtendedSLParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public ExtendedSLParser(ExtendedSLParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ExtendedSLParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[39];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 23; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 39; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
