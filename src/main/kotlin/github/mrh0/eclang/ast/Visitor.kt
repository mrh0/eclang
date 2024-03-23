package github.mrh0.eclang.ast

import github.mrh0.eclang.Util
import github.mrh0.eclang.antlr.EclangBaseVisitor
import github.mrh0.eclang.antlr.EclangParser
import github.mrh0.eclang.ast.token.*
import github.mrh0.eclang.ast.token.branch.TInlineIf
import github.mrh0.eclang.ast.token.branch.TStatementIf
import github.mrh0.eclang.ast.token.data.*
import github.mrh0.eclang.ast.token.function.TParameter
import github.mrh0.eclang.ast.token.function.call.TExprCall
import github.mrh0.eclang.ast.token.function.TFuncBlock
import github.mrh0.eclang.ast.token.function.TFuncExternal
import github.mrh0.eclang.ast.token.function.TParameters
import github.mrh0.eclang.ast.token.function.call.TStatementCall
import github.mrh0.eclang.ast.token.loop.TStatementBreak
import github.mrh0.eclang.ast.token.loop.TStatementContinue
import github.mrh0.eclang.ast.token.loop.TStatementWhile
import github.mrh0.eclang.ast.token.op.arithmetic.TAdd
import github.mrh0.eclang.ast.token.op.arithmetic.TNegate
import github.mrh0.eclang.ast.token.op.arithmetic.TSub
import github.mrh0.eclang.ast.token.op.compare.TEquals
import github.mrh0.eclang.ast.token.op.compare.TNotEquals
import github.mrh0.eclang.ast.token.op.logical.TNot
import github.mrh0.eclang.ast.token.op.logical.TNotNot
import github.mrh0.eclang.ast.token.type.TTypeByName
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.io.File

class Visitor(private val file: File) : EclangBaseVisitor<ITok>() {

    fun <T : ParserRuleContext, O : ITok?> visit(list: MutableList<T>): MutableList<O> {
        val t: MutableList<O> = mutableListOf()
        for (pt in list) t.add(visit(pt) as O)
        return t
    }

    fun <T : Token?> tvisit(list: MutableList<T>): MutableList<String> {
        val t: MutableList<String> = mutableListOf()
        for (pt in list) pt?.text
        return t
    }

    fun <T : ParserRuleContext, O : ITok?> cvisit(rule: T?): O {
        return if (rule == null) throw Exception("Undefined rule.") else visit(rule) as O
    }

    fun loc(ctx: ParserRuleContext) = Loc(ctx.start, file)

    // Program
    override fun visitProgram(ctx: EclangParser.ProgramContext): ITok = TProgram(loc(ctx), visit(ctx.functions), visit(ctx.use()))

    // Use
    // override fun visitUseFromModule(ctx: EclangParser.UseFromModuleContext): ITok {
    //     return TUseFromModule(loc(ctx), ctx.exports.map { it.text }, ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // override fun visitUseAllFromModule(ctx: EclangParser.UseAllFromModuleContext): ITok {
    //     return TUseAllFromModule(loc(ctx), ctx.from.text.substring(1, ctx.from.text.length-1))
    // }

    // Types
    override fun visitTypeByName(ctx: EclangParser.TypeByNameContext): ITok = TTypeByName(loc(ctx), ctx.text)

    // Functions
    override fun visitFunctionBlock(ctx: EclangParser.FunctionBlockContext): ITok {
        return TFuncBlock(loc(ctx), cvisit(ctx.body), ctx.name.text, TParameters(loc(ctx), visit(ctx.args)) , visit(ctx.returnType))
    }

    // override fun visitFunctionExpr(ctx: EclangParser.FunctionExprContext): ITok {
    //     return TFunc(loc(ctx), cvisit(ctx.expr()), ctx.funcPrefix()?.text ?: "", ctx.name.text, visit(ctx.args), visit(ctx.returnType))
    // }

    override fun visitFunctionExternal(ctx: EclangParser.FunctionExternalContext): ITok {
        return TFuncExternal(loc(ctx), ctx.name.text, TParameters(loc(ctx), visit(ctx.args)), visit(ctx.returnType), Util.getStringContent(ctx.externalName.text))
    }

    override fun visitBlock(ctx: EclangParser.BlockContext): ITok = TBlock(loc(ctx), visit(ctx.statements))

    override fun visitStatementReturn(ctx: EclangParser.StatementReturnContext): ITok {
        return TStatementReturn(loc(ctx), visit(ctx.expr()))
    }

    override fun visitArgumentTyped(ctx: EclangParser.ArgumentTypedContext): ITok {
        return TParameter(loc(ctx), ctx.NAME().text, visit(ctx.type()) as TTypeByName)
    }

    override fun visitFunctionCallNoArgs(ctx: EclangParser.FunctionCallNoArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, arrayListOf())
    override fun visitFunctionCallWithArgs(ctx: EclangParser.FunctionCallWithArgsContext): ITok = TStatementCall(loc(ctx), ctx.NAME().text, visit(ctx.args))

    override fun visitStatementCallFunction(ctx: EclangParser.StatementCallFunctionContext): ITok = visit(ctx.functionCall())
    override fun visitStatementCallFunctionReturn(ctx: EclangParser.StatementCallFunctionReturnContext): ITok = TStatementReturn(loc(ctx), visit(ctx.functionCall()))

    override fun visitExprCallFunction(ctx: EclangParser.ExprCallFunctionContext): ITok = TExprCall(loc(ctx), ctx.NAME().text, visit(ctx.args))

    // Expressions
    override fun visitExprNest(ctx: EclangParser.ExprNestContext): ITok = visit(ctx.expr())
    // override fun visitExprHere(ctx: EclangParser.ExprHereContext): ITok = THere(loc(ctx))

    // Primitives
    override fun visitNumberInt(ctx: EclangParser.NumberIntContext): ITok = TInteger(loc(ctx), Integer.valueOf(ctx.text.replace("_", "")))
    override fun visitNumberBin(ctx: EclangParser.NumberBinContext): ITok = TInteger(loc(ctx), ctx.text.substring(2).toInt(2))
    override fun visitNumberHex(ctx: EclangParser.NumberHexContext): ITok = TInteger(loc(ctx), Integer.decode(ctx.text))
    override fun visitNumberFloat(ctx: EclangParser.NumberFloatContext): ITok = TFloat(loc(ctx), ctx.text.toDouble())
    override fun visitPrimitiveBool(ctx: EclangParser.PrimitiveBoolContext): ITok = TBoolean(loc(ctx), ctx.BOOL().text == "true")
    override fun visitPrimitiveString(ctx: EclangParser.PrimitiveStringContext): ITok = TString(loc(ctx), Util.getStringContent(ctx.text))
    override fun visitPrimitiveAtom(ctx: EclangParser.PrimitiveAtomContext): ITok = TAtom(loc(ctx), ctx.text.substring(1).lowercase())

    //Natives
    // override fun visitExprTuple(ctx: EclangParser.ExprTupleContext): ITok = TTuple(loc(ctx), visit(ctx.values))

    // Ops
    override fun visitExprBinOp(ctx: EclangParser.ExprBinOpContext): ITok = when (ctx.binOp().text) {
        "+" -> TAdd(loc(ctx), visit(ctx.left), visit(ctx.right))
        "-" -> TSub(loc(ctx), visit(ctx.left), visit(ctx.right))

        "==" -> TEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        "!=" -> TNotEquals(loc(ctx), visit(ctx.left), visit(ctx.right))
        else -> throw NotImplementedError("Binary Operator '${ctx.binOp().text}' is not implemented.")
    }

    override fun visitExprUnOp(ctx: EclangParser.ExprUnOpContext): ITok = when (ctx.unOp().text) {
        "-" -> TNegate(loc(ctx), visit(ctx.expr()))
        "!!" -> TNotNot(loc(ctx), visit(ctx.expr()))
        "!", "not" -> TNot(loc(ctx), visit(ctx.expr()))
        else -> throw NotImplementedError("Unary Operator '${ctx.unOp().text}' is not implemented.")
    }

    // Variable
    override fun visitStatementDefine(ctx: EclangParser.StatementDefineContext): ITok = TStatementDefine(loc(ctx), ctx.NAME().text, visit(ctx.expr()))
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

    override fun visitStatementContinue(ctx: EclangParser.StatementContinueContext): ITok = TStatementContinue(loc(ctx))
    override fun visitStatementBreak(ctx: EclangParser.StatementBreakContext): ITok = TStatementBreak(loc(ctx))
}