package github.mrh0.eclang.ast

import github.mrh0.eclang.util.Util
import github.mrh0.eclang.antlr.EclangBaseVisitor
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.token.*
import github.mrh0.eclang.ast.token.accessor.TAccessor
import github.mrh0.eclang.ast.token.accessor.TAccessorNamed
import github.mrh0.eclang.ast.token.branch.TInlineIf
import github.mrh0.eclang.ast.token.branch.TStatementIf
import github.mrh0.eclang.ast.token.data.*
import github.mrh0.eclang.ast.token.data.record.TCreateRecord
import github.mrh0.eclang.ast.token.data.record.TCreateRecordTyped
import github.mrh0.eclang.ast.token.data.record.THere
import github.mrh0.eclang.ast.token.type.TTypeRecord
import github.mrh0.eclang.ast.token.function.*
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.function.call.TStatementCall
import github.mrh0.eclang.ast.token.global.*
import github.mrh0.eclang.ast.token.loop.*
import github.mrh0.eclang.ast.token.op.*
import github.mrh0.eclang.ast.token.op.arithmetic.*
import github.mrh0.eclang.ast.token.op.compare.*
import github.mrh0.eclang.ast.token.op.logical.*
import github.mrh0.eclang.ast.token.type.*
import github.mrh0.eclang.ast.token.statement.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.ErrorNode
import java.io.File

class Visitor(private val file: File) : EclangBaseVisitor<ITok>() {

    fun <T : ParserRuleContext, O : ITok?> visit(list: MutableList<T>): MutableList<O> {
        val t: MutableList<O> = mutableListOf()
        for (pt in list) t.add(visit(pt) as O)
        return t
    }

    fun <T : Token?> tvisit(list: MutableList<T>): List<String> {
        return list.map { it!!.getText() }
    }

    fun <T : ParserRuleContext, O : ITok?> cvisit(rule: T?): O {
        return if (rule == null) throw Exception("Undefined rule.") else visit(rule) as O
    }

    fun loc(ctx: ParserRuleContext) = Loc(ctx.start, file)

    // Program
    override fun visitProgram(ctx: EclangParser.ProgramContext): ITok = TProgram(loc(ctx), visit(ctx.functions), visit(ctx.uses), visit(ctx.globals))

    // Use
    // override fun visitUseFromModule(ctx: EclangParser.UseFromModuleContext): ITok {
    //     return TUseFromModule(loc(ctx), ctx.exports.map { it.text }, ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // override fun visitUseAllFromModule(ctx: EclangParser.UseAllFromModuleContext): ITok {
    //     return TUseAllFromModule(loc(ctx), ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // Records
    override fun visitGlobalRecordDefine(ctx: EclangParser.GlobalRecordDefineContext): ITok = TTypeRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types))
    override fun visitGlobalRecordDeclareDefine(ctx: EclangParser.GlobalRecordDeclareDefineContext): ITok = TTypeDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), Util.getStringContent(ctx.externalName?.text))
    override fun visitGlobalTypeRecordDeclareDefine(ctx: EclangParser.GlobalTypeRecordDeclareDefineContext): ITok  = TTypeDefDeclareRecord(loc(ctx), ctx.name.text, tvisit(ctx.names), visit(ctx.types), Util.getStringContent(ctx.externalName?.text))

    // Types
    override fun visitTypeByName(ctx: EclangParser.TypeByNameContext): ITok = TTypeByName(loc(ctx), ctx.text)

    override fun visitTypeAddressByName(ctx: EclangParser.TypeAddressByNameContext): ITok = TTypePointerByName(loc(ctx), visit(ctx.type()))

    override fun visitTypeUnion(ctx: EclangParser.TypeUnionContext): ITok = TTypeUnion(loc(ctx), visit(ctx.types))

    override fun visitTypeEnum(ctx: EclangParser.TypeEnumContext): ITok = TTypeEnum(loc(ctx), visit(ctx.types))

    override fun visitTypeNullable(ctx: EclangParser.TypeNullableContext): ITok = TTypeNullable(loc(ctx), visit(ctx.type()))

    override fun visitTypeAtom(ctx: EclangParser.TypeAtomContext): ITok = TTypeAtom(loc(ctx), ctx.text)

    override fun visitTypeNest(ctx: EclangParser.TypeNestContext): ITok = visit(ctx.type())

    override fun visitTypeGeneric(ctx: EclangParser.TypeGenericContext): ITok = TTypeGeneric(loc(ctx), ctx.NAME().text)

    override fun visitTypeTypeOf(ctx: EclangParser.TypeTypeOfContext): ITok = TTypeOf(loc(ctx), visit(ctx.expr()))

    override fun visitTypeArray(ctx: EclangParser.TypeArrayContext): ITok =
        if(ctx.text.endsWith('c')) TTypeCArray(loc(ctx), visit(ctx.type())) else TTypeArray(loc(ctx), visit(ctx.type()))

    override fun visitTypeThrows(ctx: EclangParser.TypeThrowsContext): ITok = TTypeThrows(loc(ctx), visit(ctx.left), visit(ctx.throwing))

    // Functions
    override fun visitFunctionBlock(ctx: EclangParser.FunctionBlockContext): ITok {
        return TFuncBlock(loc(ctx), cvisit(ctx.body), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), if(ctx.returnType == null) null else visit(ctx.returnType))
    }

    override fun visitFunctionInline(ctx: EclangParser.FunctionInlineContext): ITok {
        return TFuncInline(loc(ctx), cvisit(ctx.expr()), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), if(ctx.returnType == null) null else visit(ctx.returnType))
    }

    override fun visitFunctionDeclare(ctx: EclangParser.FunctionDeclareContext): ITok {
        return TFuncExternal(loc(ctx), ctx.name.text, TParameters(loc(ctx), visit(ctx.params)), if(ctx.returnType == null) null else visit(ctx.returnType), Util.getStringContent(ctx.externalName?.text))
    }

    override fun visitBlock(ctx: EclangParser.BlockContext): ITok = TBlock(loc(ctx), visit(ctx.statements))

    override fun visitStatementReturn(ctx: EclangParser.StatementReturnContext): ITok {
        return TStatementReturn(loc(ctx), visit(ctx.expr()))
    }

    override fun visitParameterTyped(ctx: EclangParser.ParameterTypedContext): ITok = TParameterTyped(loc(ctx), ctx.NAME().text, visit(ctx.type()))

    override fun visitParameterDefault(ctx: EclangParser.ParameterDefaultContext): ITok = TParameterDefault(loc(ctx), ctx.NAME().text, visit(ctx.expr()))

    override fun visitParameterTypedDefault(ctx: EclangParser.ParameterTypedDefaultContext): ITok = TParameterTypedDefault(loc(ctx), ctx.NAME().text, visit(ctx.type()), visit(ctx.expr()))

    override fun visitExprFunctionCallNoArgs(ctx: EclangParser.ExprFunctionCallNoArgsContext): ITok = TExprCall(loc(ctx), ctx.NAME().text, visit(ctx.args))
    override fun visitExprFunctionCallWithArgs(ctx: EclangParser.ExprFunctionCallWithArgsContext): ITok = TExprCall(loc(ctx), ctx.NAME().text, visit(ctx.args))

    override fun visitStatementFunctionCallNoArgs(ctx: EclangParser.StatementFunctionCallNoArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, visit(ctx.args))
    override fun visitStatementFunctionCallWithArgs(ctx: EclangParser.StatementFunctionCallWithArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, visit(ctx.args))

    // Expressions
    override fun visitExprNest(ctx: EclangParser.ExprNestContext): ITok = TExprNest(loc(ctx), visit(ctx.expr()))

    override fun visitExprAccessName(ctx: EclangParser.ExprAccessNameContext): ITok = TAccessorNamed(loc(ctx), visit(ctx.accesssed), ctx.NAME().text)
    override fun visitExprAccessor(ctx: EclangParser.ExprAccessorContext): ITok = TAccessor(loc(ctx), visit(ctx.accesssed), visit(ctx.index))

    override fun visitExprAddressOf(ctx: EclangParser.ExprAddressOfContext): ITok = TAddressOf(loc(ctx), visit(ctx.expr()))
    override fun visitExprSizeOf(ctx: EclangParser.ExprSizeOfContext): ITok = TSizeOf(loc(ctx), visit(ctx.type()))
    override fun visitExprHere(ctx: EclangParser.ExprHereContext): ITok = THere(loc(ctx))

    // Primitives
    override fun visitNumberInt(ctx: EclangParser.NumberIntContext): ITok = TInteger(loc(ctx), ctx.text.replace("_", "").replace("i", "").toInt())
    override fun visitNumberLong(ctx: EclangParser.NumberLongContext): ITok = TLong(loc(ctx), ctx.text.replace("_", "").dropLast(1).toLong())
    override fun visitNumberShort(ctx: EclangParser.NumberShortContext): ITok = TShort(loc(ctx), ctx.text.replace("_", "").dropLast(1).toShort())
    override fun visitNumberChar(ctx: EclangParser.NumberCharContext): ITok = TCharNum(loc(ctx), ctx.text.replace("_", "").dropLast(1).toInt())

    override fun visitNumberUInt(ctx: EclangParser.NumberUIntContext): ITok = TUnsignedInteger(loc(ctx), ctx.text.replace("_", "").replace("i", "").replace("u", "").toInt())
    override fun visitNumberULong(ctx: EclangParser.NumberULongContext): ITok = TUnsignedLong(loc(ctx), ctx.text.replace("_", "").dropLast(2).toLong())
    override fun visitNumberUShort(ctx: EclangParser.NumberUShortContext): ITok = TUnsignedShort(loc(ctx), ctx.text.replace("_", "").dropLast(2).toShort())
    override fun visitNumberUChar(ctx: EclangParser.NumberUCharContext): ITok = TUnsignedCharNum(loc(ctx), ctx.text.replace("_", "").dropLast(2).toInt())

    override fun visitNumberBin(ctx: EclangParser.NumberBinContext): ITok = TInteger(loc(ctx), ctx.text.substring(2).toInt(2))
    override fun visitNumberHex(ctx: EclangParser.NumberHexContext): ITok = TInteger(loc(ctx), Integer.decode(ctx.text))
    override fun visitNumberFloat(ctx: EclangParser.NumberFloatContext): ITok = TFloat(loc(ctx), ctx.text.toFloat())
    override fun visitNumberDouble(ctx: EclangParser.NumberDoubleContext): ITok = TDouble(loc(ctx), ctx.text.toDouble())

    override fun visitPrimitiveBool(ctx: EclangParser.PrimitiveBoolContext): ITok = TBoolean(loc(ctx), ctx.BOOL().text == "true")
    //override fun visitPrimitiveCString(ctx: EclangParser.PrimitiveCStringContext): ITok = TCString(loc(ctx), Util.getCStringContent(ctx.text) ?: "")
    override fun visitPrimitiveString(ctx: EclangParser.PrimitiveStringContext): ITok =
        if(ctx.text.endsWith('c')) TCString(loc(ctx), Util.getCStringContent(ctx.text) ?: "") else TString(loc(ctx), Util.getStringContent(ctx.text) ?: "")
    override fun visitPrimitiveAtom(ctx: EclangParser.PrimitiveAtomContext): ITok = TAtom(loc(ctx), ctx.text.substring(1).lowercase())
    override fun visitPrimitiveChar(ctx: EclangParser.PrimitiveCharContext): ITok = TChar(loc(ctx), ctx.text)

    override fun visitExprNull(ctx: EclangParser.ExprNullContext): ITok = TNull(loc(ctx))

    override fun visitExprCreateRecord(ctx: EclangParser.ExprCreateRecordContext): ITok = if (ctx.recordType == null) TCreateRecord(loc(ctx), visit(ctx.expr())) else TCreateRecordTyped(loc(ctx), ctx.recordType.text, visit(ctx.expr()))

    override fun visitExprCreateArray(ctx: EclangParser.ExprCreateArrayContext): ITok =
        if(ctx.text.endsWith('c')) TCreateCArray(loc(ctx), visit(ctx.expr())) else TCreateArray(loc(ctx), visit(ctx.expr()))
    //Natives
    // override fun visitExprTuple(ctx: EclangParser.ExprTupleContext): ITok = TTuple(loc(ctx), visit(ctx.values))

    // Ops
    override fun visitExprBinOp(ctx: EclangParser.ExprBinOpContext): ITok = when (ctx.binOp().text) {
        "+" -> TAdd(loc(ctx), visit(ctx.left), visit(ctx.right))
        "-" -> TSub(loc(ctx), visit(ctx.left), visit(ctx.right))
        "*" -> TMul(loc(ctx), visit(ctx.left), visit(ctx.right))
        "/" -> TDiv(loc(ctx), visit(ctx.left), visit(ctx.right))
        "**" -> TPow(loc(ctx), visit(ctx.left), visit(ctx.right))
        "%" -> TMod(loc(ctx), visit(ctx.left), visit(ctx.right))

        "==" -> TEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "===" -> TEqualsEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        ">=" -> TGreaterOrEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        ">" -> TGreaterThan(loc(ctx), visit(ctx.left), visit(ctx.right))
        "<=" -> TLessOrEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "<" -> TLessThan(loc(ctx), visit(ctx.left), visit(ctx.right))
        "!=" -> TNotEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "!==" -> TNotEqualsEquals(loc(ctx), visit(ctx.left), visit(ctx.right))

        "??" -> TNullishCoalescing(loc(ctx), visit(ctx.left), visit(ctx.right))

        "&&", "and" -> TAnd(loc(ctx), visit(ctx.left), visit(ctx.right))
        "||", "or" -> TOr(loc(ctx), visit(ctx.left), visit(ctx.right))
        "^^", "xor" -> TXOr(loc(ctx), visit(ctx.left), visit(ctx.right))
        else -> throw NotImplementedError("Binary Operator '${ctx.binOp().text}' is not implemented.")
    }

    override fun visitExprUnOp(ctx: EclangParser.ExprUnOpContext): ITok = when (ctx.unOp().text) {
        "-" -> TNegate(loc(ctx), visit(ctx.expr()))
        "!!" -> TNotNot(loc(ctx), visit(ctx.expr()))
        "!", "not" -> TNot(loc(ctx), visit(ctx.expr()))
        "@" -> TReadPointer(loc(ctx), visit(ctx.expr()))
        else -> throw NotImplementedError("Unary Operator '${ctx.unOp().text}' is not implemented.")
    }

    override fun visitExprAs(ctx: EclangParser.ExprAsContext): ITok = TCast(loc(ctx), visit(ctx.expr()), visit(ctx.type()))
    override fun visitExprAsUnsafe(ctx: EclangParser.ExprAsUnsafeContext): ITok = TCastUnsafe(loc(ctx), visit(ctx.expr()), visit(ctx.type()))
    override fun visitExprCastNotNull(ctx: EclangParser.ExprCastNotNullContext): ITok = TCastNotNull(loc(ctx), visit(ctx.expr()))
    // Variable
    //override fun visitGlobalDefine(ctx: EclangParser.GlobalDefineContext): ITok = TStatementDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    //override fun visitGlobalDefineConst(ctx: EclangParser.GlobalDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))

    override fun visitGlobalDefine(ctx: EclangParser.GlobalDefineContext): ITok = TGlobalDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitGlobalDefineConst(ctx: EclangParser.GlobalDefineConstContext): ITok = TGlobalDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitGlobalDeclareConst(ctx: EclangParser.GlobalDeclareConstContext): ITok = TGlobalDeclareConst(loc(ctx), ctx.NAME().text, visit(ctx.type()), if (ctx.externalName != null) Util.getStringContent(ctx.externalName.text) else null)
    override fun visitGlobalDefineTyped(ctx: EclangParser.GlobalDefineTypedContext): ITok = TGlobalDefineTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitGlobalDefineConstTyped(ctx: EclangParser.GlobalDefineConstTypedContext): ITok = TGlobalDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitGlobalTypeDefine(ctx: EclangParser.GlobalTypeDefineContext): ITok = TGlobalTypeDefine(loc(ctx), ctx.NAME().text, visit(ctx.type()))
    override fun visitUse(ctx: EclangParser.UseContext): ITok = TGlobalUse(loc(ctx), Util.getStringContent(ctx.from.text) ?: "")

    override fun visitStatementDefine(ctx: EclangParser.StatementDefineContext): ITok = TStatementDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineConst(ctx: EclangParser.StatementDefineConstContext): ITok = TStatementDefineConst(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitStatementDefineTyped(ctx: EclangParser.StatementDefineTypedContext): ITok = TStatementDefineTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitStatementDefineConstTyped(ctx: EclangParser.StatementDefineConstTypedContext): ITok = TStatementDefineConstTyped(loc(ctx), ctx.NAME().text, visit(ctx.expr()), visit(ctx.type()))
    override fun visitStatementAssignment(ctx: EclangParser.StatementAssignmentContext): ITok = TStatementAssign(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
    override fun visitExprNamed(ctx: EclangParser.ExprNamedContext): ITok = TNamed(loc(ctx), ctx.NAME().text)

    // Branches
    override fun visitStatementIf(ctx: EclangParser.StatementIfContext): ITok {
        return TStatementIf(loc(ctx), visit(ctx.conditions), visit(ctx.bodies), if(ctx.elseBody == null) null else visit(ctx.elseBody))
    }

    override fun visitExprInlineIf(ctx: EclangParser.ExprInlineIfContext): ITok {
        return TInlineIf(loc(ctx), visit(ctx.condition), visit(ctx.body), visit(ctx.elseBody))
    }

    // Loops
    override fun visitStatementWhile(ctx: EclangParser.StatementWhileContext): ITok {
        return TStatementWhile(loc(ctx), visit(ctx.condition), visit(ctx.body), if(ctx.elseBody == null) null else visit(ctx.elseBody))
    }

    override fun visitStatementForInRange(ctx: EclangParser.StatementForInRangeContext): ITok {
        return TStatementForeachInRange(loc(ctx), ctx.NAME().text, visit(ctx.lower), visit(ctx.upper), visit(ctx.body), null)
    }

    override fun visitStatementForIn(ctx: EclangParser.StatementForInContext): ITok {
        return TStatementForeachIn(loc(ctx), ctx.NAME().text, visit(ctx.iterable), visit(ctx.body), null)
    }

    override fun visitStatementContinue(ctx: EclangParser.StatementContinueContext): ITok = TStatementContinue(loc(ctx))
    override fun visitStatementBreak(ctx: EclangParser.StatementBreakContext): ITok = TStatementBreak(loc(ctx))
    override fun visitStatementPass(ctx: EclangParser.StatementPassContext): ITok = TPass(loc(ctx))
    override fun visitStatementDefer(ctx: EclangParser.StatementDeferContext): ITok = TDefer(loc(ctx),visit(ctx.statement()))
    override fun visitStatementDeferDo(ctx: EclangParser.StatementDeferDoContext): ITok = TDefer(loc(ctx),visit(ctx.block()))
}