/**
 * Bindings for Dear ImGui 1.93.0 through cimgui, its C API: immediate-mode user interfaces.
 * https://github.com/ocornut/imgui, https://github.com/cimgui/cimgui
 *
 * Generated from cimgui's metadata by bindings/cimgui/generate.py; run it again rather than changing this
 * file. Names are cimgui's: ImGui::Button is igButton, ImDrawList::AddLine is ImDrawList_AddLine (taking the
 * list first). Overloads that cimgui names apart (igPushID_Str, igPushID_Int) are also eclang overloads of the
 * plain name (igPushID). See README.md.
 */
use "define:CIMGUI_DEFINE_ENUMS_AND_STRUCTS"
use "cimgui.h"
use "float.h"
use "lib:cimgui"

/** The largest Float (C's FLT_MAX): ImGui's "no limit". */
declare val FLT_MAX as Float
/** The smallest positive normal Float (C's FLT_MIN): a width of -FLT_MIN means "to the right edge". */
declare val FLT_MIN as Float

// ---------------------------------------------------------------------------------------------------------------------
// Types
// ---------------------------------------------------------------------------------------------------------------------

declare type ImDrawIdx as UShort
declare type ImDrawTextFlags as Int
declare type ImFontAtlasRectId as Int
declare type ImGuiID as UInt
declare type ImGuiKeyChord as Int
declare type ImGuiSelectionUserData as Long
declare type ImS16 as Short
declare type ImS8 as Char
declare type ImTextureID as ULong
declare type ImU16 as UShort
declare type ImU32 as UInt
declare type ImU8 as UChar
declare type ImWchar as UShort
declare type ImWchar16 as UShort
declare type ImDrawCallback as (parent_list: @ImDrawList?, cmd: @ImDrawCmd?) => None
declare type ImGuiInputTextCallback as (data: @ImGuiInputTextCallbackData?) => Int
declare type ImGuiMemAllocFunc as (sz: Size, user_data: @Any?) => @Any?
declare type ImGuiMemFreeFunc as (ptr: @Any?, user_data: @Any?) => None
declare type ImGuiSizeCallback as (data: @ImGuiSizeCallbackData?) => None
declare rec ImDrawListSharedData
declare rec ImFontAtlasBuilder
declare rec ImFontBakedSrc
declare rec ImFontLoader
declare rec ImGuiContext
declare rec ImGuiTextFilterItem
/** An ImVector<ImDrawCmd>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImDrawCmd as
    Size: Int
    Capacity: Int
    Data: ImDrawCmd[]c?
/** An ImVector<ImDrawIdx>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImDrawIdx as
    Size: Int
    Capacity: Int
    Data: ImDrawIdx[]c?
/** An ImVector<ImDrawList*>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImDrawListPtr as
    Size: Int
    Capacity: Int
    Data: @ImDrawList[]c?
/** An ImVector<ImDrawListSharedData*>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImDrawListSharedDataPtr as
    Size: Int
    Capacity: Int
    Data: @ImDrawListSharedData[]c?
/** An ImVector<ImDrawVert>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImDrawVert as
    Size: Int
    Capacity: Int
    Data: ImDrawVert[]c?
/** An ImVector<ImFontConfig>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImFontConfig as
    Size: Int
    Capacity: Int
    Data: ImFontConfig[]c?
/** An ImVector<ImFontConfig*>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImFontConfigPtr as
    Size: Int
    Capacity: Int
    Data: @ImFontConfig[]c?
/** An ImVector<ImFontGlyph>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImFontGlyph as
    Size: Int
    Capacity: Int
    Data: ImFontGlyph[]c?
/** An ImVector<ImFont*>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImFontPtr as
    Size: Int
    Capacity: Int
    Data: @ImFont[]c?
/** An ImVector<ImGuiSelectionRequest>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImGuiSelectionRequest as
    Size: Int
    Capacity: Int
    Data: ImGuiSelectionRequest[]c?
/** An ImVector<ImGuiStoragePair>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImGuiStoragePair as
    Size: Int
    Capacity: Int
    Data: ImGuiStoragePair[]c?
/** An ImVector<ImTextureData*>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImTextureDataPtr as
    Size: Int
    Capacity: Int
    Data: @ImTextureData[]c?
/** An ImVector<ImTextureRect>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImTextureRect as
    Size: Int
    Capacity: Int
    Data: ImTextureRect[]c?
/** An ImVector<ImU16>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImU16 as
    Size: Int
    Capacity: Int
    Data: ImU16[]c?
/** An ImVector<ImU32>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImU32 as
    Size: Int
    Capacity: Int
    Data: ImU32[]c?
/** An ImVector<ImWchar>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_ImWchar as
    Size: Int
    Capacity: Int
    Data: ImWchar[]c?
/** An ImVector<char>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_char as
    Size: Int
    Capacity: Int
    Data: Char[]c?
/** An ImVector<float>: Size elements of Data (null while empty), with room for Capacity. */
declare mut rec ImVector_float as
    Size: Int
    Capacity: Int
    Data: Float[]c?

// ---------------------------------------------------------------------------------------------------------------------
// Enums: numbers named as in C, of the int type ImGui gives them (ImGuiWindowFlags), combined with `|`
// ---------------------------------------------------------------------------------------------------------------------

declare type ImGuiWindowFlags as Int
declare val ImGuiWindowFlags_None as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoTitleBar as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoResize as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoMove as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoScrollbar as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoScrollWithMouse as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoCollapse as ImGuiWindowFlags
declare val ImGuiWindowFlags_AlwaysAutoResize as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoBackground as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoSavedSettings as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoMouseInputs as ImGuiWindowFlags
declare val ImGuiWindowFlags_MenuBar as ImGuiWindowFlags
declare val ImGuiWindowFlags_HorizontalScrollbar as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoFocusOnAppearing as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoBringToFrontOnFocus as ImGuiWindowFlags
declare val ImGuiWindowFlags_AlwaysVerticalScrollbar as ImGuiWindowFlags
declare val ImGuiWindowFlags_AlwaysHorizontalScrollbar as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoNavInputs as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoNavFocus as ImGuiWindowFlags
declare val ImGuiWindowFlags_UnsavedDocument as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoNav as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoDecoration as ImGuiWindowFlags
declare val ImGuiWindowFlags_NoInputs as ImGuiWindowFlags
declare val ImGuiWindowFlags_ChildWindow as ImGuiWindowFlags
declare val ImGuiWindowFlags_Tooltip as ImGuiWindowFlags
declare val ImGuiWindowFlags_Popup as ImGuiWindowFlags
declare val ImGuiWindowFlags_Modal as ImGuiWindowFlags
declare val ImGuiWindowFlags_ChildMenu as ImGuiWindowFlags

declare type ImGuiChildFlags as Int
declare val ImGuiChildFlags_None as ImGuiChildFlags
declare val ImGuiChildFlags_Borders as ImGuiChildFlags
declare val ImGuiChildFlags_AlwaysUseWindowPadding as ImGuiChildFlags
declare val ImGuiChildFlags_ResizeX as ImGuiChildFlags
declare val ImGuiChildFlags_ResizeY as ImGuiChildFlags
declare val ImGuiChildFlags_AutoResizeX as ImGuiChildFlags
declare val ImGuiChildFlags_AutoResizeY as ImGuiChildFlags
declare val ImGuiChildFlags_AlwaysAutoResize as ImGuiChildFlags
declare val ImGuiChildFlags_FrameStyle as ImGuiChildFlags
declare val ImGuiChildFlags_NavFlattened as ImGuiChildFlags

declare type ImGuiItemFlags as Int
declare val ImGuiItemFlags_None as ImGuiItemFlags
declare val ImGuiItemFlags_NoTabStop as ImGuiItemFlags
declare val ImGuiItemFlags_NoNav as ImGuiItemFlags
declare val ImGuiItemFlags_NoNavDefaultFocus as ImGuiItemFlags
declare val ImGuiItemFlags_ButtonRepeat as ImGuiItemFlags
declare val ImGuiItemFlags_AutoClosePopups as ImGuiItemFlags
declare val ImGuiItemFlags_AllowDuplicateId as ImGuiItemFlags
declare val ImGuiItemFlags_Disabled as ImGuiItemFlags
declare val ImGuiItemFlags_LiveEditOnInputText as ImGuiItemFlags
declare val ImGuiItemFlags_LiveEditOnInputScalar as ImGuiItemFlags
declare val ImGuiItemFlags_LiveEditOnInput as ImGuiItemFlags
declare val ImGuiItemFlags_MixedValue as ImGuiItemFlags

declare type ImGuiInputTextFlags as Int
declare val ImGuiInputTextFlags_None as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CharsDecimal as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CharsHexadecimal as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CharsScientific as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CharsUppercase as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CharsNoBlank as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_AllowTabInput as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_EnterReturnsTrue as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_EscapeClearsAll as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CtrlEnterForNewLine as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_ReadOnly as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_Password as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_AlwaysOverwrite as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_AutoSelectAll as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_ParseEmptyRefVal as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_DisplayEmptyRefVal as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_NoHorizontalScroll as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_NoUndoRedo as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_ElideLeft as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CallbackCompletion as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CallbackHistory as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CallbackAlways as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CallbackCharFilter as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CallbackResize as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_CallbackEdit as ImGuiInputTextFlags
declare val ImGuiInputTextFlags_WordWrap as ImGuiInputTextFlags

declare type ImGuiTreeNodeFlags as Int
declare val ImGuiTreeNodeFlags_None as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_Selected as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_Framed as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_AllowOverlap as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_NoTreePushOnOpen as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_NoAutoOpenOnLog as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_DefaultOpen as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_OpenOnDoubleClick as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_OpenOnArrow as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_Leaf as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_Bullet as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_FramePadding as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_SpanAvailWidth as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_SpanFullWidth as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_SpanLabelWidth as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_SpanAllColumns as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_LabelSpanAllColumns as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_NavLeftJumpsToParent as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_CollapsingHeader as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_DrawLinesNone as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_DrawLinesFull as ImGuiTreeNodeFlags
declare val ImGuiTreeNodeFlags_DrawLinesToNodes as ImGuiTreeNodeFlags

declare type ImGuiPopupFlags as Int
declare val ImGuiPopupFlags_None as ImGuiPopupFlags
declare val ImGuiPopupFlags_MouseButtonLeft as ImGuiPopupFlags
declare val ImGuiPopupFlags_MouseButtonRight as ImGuiPopupFlags
declare val ImGuiPopupFlags_MouseButtonMiddle as ImGuiPopupFlags
declare val ImGuiPopupFlags_NoReopen as ImGuiPopupFlags
declare val ImGuiPopupFlags_NoOpenOverExistingPopup as ImGuiPopupFlags
declare val ImGuiPopupFlags_NoOpenOverItems as ImGuiPopupFlags
declare val ImGuiPopupFlags_AnyPopupId as ImGuiPopupFlags
declare val ImGuiPopupFlags_AnyPopupLevel as ImGuiPopupFlags
declare val ImGuiPopupFlags_AnyPopup as ImGuiPopupFlags
declare val ImGuiPopupFlags_MouseButtonShift_ as ImGuiPopupFlags
declare val ImGuiPopupFlags_MouseButtonMask_ as ImGuiPopupFlags
declare val ImGuiPopupFlags_InvalidMask_ as ImGuiPopupFlags

declare type ImGuiSelectableFlags as Int
declare val ImGuiSelectableFlags_None as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_NoAutoClosePopups as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_SpanAllColumns as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_AllowDoubleClick as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_Disabled as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_AllowOverlap as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_Highlight as ImGuiSelectableFlags
declare val ImGuiSelectableFlags_SelectOnNav as ImGuiSelectableFlags

declare type ImGuiComboFlags as Int
declare val ImGuiComboFlags_None as ImGuiComboFlags
declare val ImGuiComboFlags_PopupAlignLeft as ImGuiComboFlags
declare val ImGuiComboFlags_HeightSmall as ImGuiComboFlags
declare val ImGuiComboFlags_HeightRegular as ImGuiComboFlags
declare val ImGuiComboFlags_HeightLarge as ImGuiComboFlags
declare val ImGuiComboFlags_HeightLargest as ImGuiComboFlags
declare val ImGuiComboFlags_NoArrowButton as ImGuiComboFlags
declare val ImGuiComboFlags_NoPreview as ImGuiComboFlags
declare val ImGuiComboFlags_WidthFitPreview as ImGuiComboFlags
declare val ImGuiComboFlags_HeightMask_ as ImGuiComboFlags

declare type ImGuiTabBarFlags as Int
declare val ImGuiTabBarFlags_None as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_Reorderable as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_AutoSelectNewTabs as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_TabListPopupButton as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_NoCloseWithMiddleMouseButton as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_NoTabListScrollingButtons as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_NoTooltip as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_DrawSelectedOverline as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_FittingPolicyMixed as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_FittingPolicyShrink as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_FittingPolicyScroll as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_FittingPolicyMask_ as ImGuiTabBarFlags
declare val ImGuiTabBarFlags_FittingPolicyDefault_ as ImGuiTabBarFlags

declare type ImGuiTabItemFlags as Int
declare val ImGuiTabItemFlags_None as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_UnsavedDocument as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_SetSelected as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_NoCloseWithMiddleMouseButton as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_NoPushId as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_NoTooltip as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_NoReorder as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_Leading as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_Trailing as ImGuiTabItemFlags
declare val ImGuiTabItemFlags_NoAssumedClosure as ImGuiTabItemFlags

declare type ImGuiFocusedFlags as Int
declare val ImGuiFocusedFlags_None as ImGuiFocusedFlags
declare val ImGuiFocusedFlags_ChildWindows as ImGuiFocusedFlags
declare val ImGuiFocusedFlags_RootWindow as ImGuiFocusedFlags
declare val ImGuiFocusedFlags_AnyWindow as ImGuiFocusedFlags
declare val ImGuiFocusedFlags_NoPopupHierarchy as ImGuiFocusedFlags
declare val ImGuiFocusedFlags_RootAndChildWindows as ImGuiFocusedFlags

declare type ImGuiHoveredFlags as Int
declare val ImGuiHoveredFlags_None as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_ChildWindows as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_RootWindow as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AnyWindow as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_NoPopupHierarchy as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AllowWhenBlockedByPopup as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AllowWhenBlockedByActiveItem as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AllowWhenOverlappedByItem as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AllowWhenOverlappedByWindow as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AllowWhenDisabled as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_NoNavOverride as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_AllowWhenOverlapped as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_RectOnly as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_RootAndChildWindows as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_ForTooltip as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_Stationary as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_DelayNone as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_DelayShort as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_DelayNormal as ImGuiHoveredFlags
declare val ImGuiHoveredFlags_NoSharedDelay as ImGuiHoveredFlags

declare type ImGuiDragDropFlags as Int
declare val ImGuiDragDropFlags_None as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_SourceNoPreviewTooltip as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_SourceNoDisableHover as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_SourceNoHoldToOpenOthers as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_SourceAllowNullID as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_SourceExtern as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_PayloadAutoExpire as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_PayloadNoCrossContext as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_PayloadNoCrossProcess as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_AcceptBeforeDelivery as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_AcceptNoDrawDefaultRect as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_AcceptNoPreviewTooltip as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_AcceptDrawAsHovered as ImGuiDragDropFlags
declare val ImGuiDragDropFlags_AcceptPeekOnly as ImGuiDragDropFlags

declare type ImGuiDataType as Int
declare val ImGuiDataType_S8 as ImGuiDataType
declare val ImGuiDataType_U8 as ImGuiDataType
declare val ImGuiDataType_S16 as ImGuiDataType
declare val ImGuiDataType_U16 as ImGuiDataType
declare val ImGuiDataType_S32 as ImGuiDataType
declare val ImGuiDataType_U32 as ImGuiDataType
declare val ImGuiDataType_S64 as ImGuiDataType
declare val ImGuiDataType_U64 as ImGuiDataType
declare val ImGuiDataType_Float as ImGuiDataType
declare val ImGuiDataType_Double as ImGuiDataType
declare val ImGuiDataType_Bool as ImGuiDataType
declare val ImGuiDataType_String as ImGuiDataType
declare val ImGuiDataType_COUNT as ImGuiDataType

declare type ImGuiDir as Int
declare val ImGuiDir_None as ImGuiDir
declare val ImGuiDir_Left as ImGuiDir
declare val ImGuiDir_Right as ImGuiDir
declare val ImGuiDir_Up as ImGuiDir
declare val ImGuiDir_Down as ImGuiDir
declare val ImGuiDir_COUNT as ImGuiDir

declare type ImGuiSortDirection as Int
declare val ImGuiSortDirection_None as ImGuiSortDirection
declare val ImGuiSortDirection_Ascending as ImGuiSortDirection
declare val ImGuiSortDirection_Descending as ImGuiSortDirection

declare type ImGuiKey as Int
declare val ImGuiKey_None as ImGuiKey
declare val ImGuiKey_NamedKey_BEGIN as ImGuiKey
declare val ImGuiKey_Tab as ImGuiKey
declare val ImGuiKey_LeftArrow as ImGuiKey
declare val ImGuiKey_RightArrow as ImGuiKey
declare val ImGuiKey_UpArrow as ImGuiKey
declare val ImGuiKey_DownArrow as ImGuiKey
declare val ImGuiKey_PageUp as ImGuiKey
declare val ImGuiKey_PageDown as ImGuiKey
declare val ImGuiKey_Home as ImGuiKey
declare val ImGuiKey_End as ImGuiKey
declare val ImGuiKey_Insert as ImGuiKey
declare val ImGuiKey_Delete as ImGuiKey
declare val ImGuiKey_Backspace as ImGuiKey
declare val ImGuiKey_Space as ImGuiKey
declare val ImGuiKey_Enter as ImGuiKey
declare val ImGuiKey_Escape as ImGuiKey
declare val ImGuiKey_LeftCtrl as ImGuiKey
declare val ImGuiKey_LeftShift as ImGuiKey
declare val ImGuiKey_LeftAlt as ImGuiKey
declare val ImGuiKey_LeftSuper as ImGuiKey
declare val ImGuiKey_RightCtrl as ImGuiKey
declare val ImGuiKey_RightShift as ImGuiKey
declare val ImGuiKey_RightAlt as ImGuiKey
declare val ImGuiKey_RightSuper as ImGuiKey
declare val ImGuiKey_Menu as ImGuiKey
declare val ImGuiKey_0 as ImGuiKey
declare val ImGuiKey_1 as ImGuiKey
declare val ImGuiKey_2 as ImGuiKey
declare val ImGuiKey_3 as ImGuiKey
declare val ImGuiKey_4 as ImGuiKey
declare val ImGuiKey_5 as ImGuiKey
declare val ImGuiKey_6 as ImGuiKey
declare val ImGuiKey_7 as ImGuiKey
declare val ImGuiKey_8 as ImGuiKey
declare val ImGuiKey_9 as ImGuiKey
declare val ImGuiKey_A as ImGuiKey
declare val ImGuiKey_B as ImGuiKey
declare val ImGuiKey_C as ImGuiKey
declare val ImGuiKey_D as ImGuiKey
declare val ImGuiKey_E as ImGuiKey
declare val ImGuiKey_F as ImGuiKey
declare val ImGuiKey_G as ImGuiKey
declare val ImGuiKey_H as ImGuiKey
declare val ImGuiKey_I as ImGuiKey
declare val ImGuiKey_J as ImGuiKey
declare val ImGuiKey_K as ImGuiKey
declare val ImGuiKey_L as ImGuiKey
declare val ImGuiKey_M as ImGuiKey
declare val ImGuiKey_N as ImGuiKey
declare val ImGuiKey_O as ImGuiKey
declare val ImGuiKey_P as ImGuiKey
declare val ImGuiKey_Q as ImGuiKey
declare val ImGuiKey_R as ImGuiKey
declare val ImGuiKey_S as ImGuiKey
declare val ImGuiKey_T as ImGuiKey
declare val ImGuiKey_U as ImGuiKey
declare val ImGuiKey_V as ImGuiKey
declare val ImGuiKey_W as ImGuiKey
declare val ImGuiKey_X as ImGuiKey
declare val ImGuiKey_Y as ImGuiKey
declare val ImGuiKey_Z as ImGuiKey
declare val ImGuiKey_F1 as ImGuiKey
declare val ImGuiKey_F2 as ImGuiKey
declare val ImGuiKey_F3 as ImGuiKey
declare val ImGuiKey_F4 as ImGuiKey
declare val ImGuiKey_F5 as ImGuiKey
declare val ImGuiKey_F6 as ImGuiKey
declare val ImGuiKey_F7 as ImGuiKey
declare val ImGuiKey_F8 as ImGuiKey
declare val ImGuiKey_F9 as ImGuiKey
declare val ImGuiKey_F10 as ImGuiKey
declare val ImGuiKey_F11 as ImGuiKey
declare val ImGuiKey_F12 as ImGuiKey
declare val ImGuiKey_F13 as ImGuiKey
declare val ImGuiKey_F14 as ImGuiKey
declare val ImGuiKey_F15 as ImGuiKey
declare val ImGuiKey_F16 as ImGuiKey
declare val ImGuiKey_F17 as ImGuiKey
declare val ImGuiKey_F18 as ImGuiKey
declare val ImGuiKey_F19 as ImGuiKey
declare val ImGuiKey_F20 as ImGuiKey
declare val ImGuiKey_F21 as ImGuiKey
declare val ImGuiKey_F22 as ImGuiKey
declare val ImGuiKey_F23 as ImGuiKey
declare val ImGuiKey_F24 as ImGuiKey
declare val ImGuiKey_Apostrophe as ImGuiKey
declare val ImGuiKey_Comma as ImGuiKey
declare val ImGuiKey_Minus as ImGuiKey
declare val ImGuiKey_Period as ImGuiKey
declare val ImGuiKey_Slash as ImGuiKey
declare val ImGuiKey_Semicolon as ImGuiKey
declare val ImGuiKey_Equal as ImGuiKey
declare val ImGuiKey_LeftBracket as ImGuiKey
declare val ImGuiKey_Backslash as ImGuiKey
declare val ImGuiKey_RightBracket as ImGuiKey
declare val ImGuiKey_GraveAccent as ImGuiKey
declare val ImGuiKey_CapsLock as ImGuiKey
declare val ImGuiKey_ScrollLock as ImGuiKey
declare val ImGuiKey_NumLock as ImGuiKey
declare val ImGuiKey_PrintScreen as ImGuiKey
declare val ImGuiKey_Pause as ImGuiKey
declare val ImGuiKey_Keypad0 as ImGuiKey
declare val ImGuiKey_Keypad1 as ImGuiKey
declare val ImGuiKey_Keypad2 as ImGuiKey
declare val ImGuiKey_Keypad3 as ImGuiKey
declare val ImGuiKey_Keypad4 as ImGuiKey
declare val ImGuiKey_Keypad5 as ImGuiKey
declare val ImGuiKey_Keypad6 as ImGuiKey
declare val ImGuiKey_Keypad7 as ImGuiKey
declare val ImGuiKey_Keypad8 as ImGuiKey
declare val ImGuiKey_Keypad9 as ImGuiKey
declare val ImGuiKey_KeypadDecimal as ImGuiKey
declare val ImGuiKey_KeypadDivide as ImGuiKey
declare val ImGuiKey_KeypadMultiply as ImGuiKey
declare val ImGuiKey_KeypadSubtract as ImGuiKey
declare val ImGuiKey_KeypadAdd as ImGuiKey
declare val ImGuiKey_KeypadEnter as ImGuiKey
declare val ImGuiKey_KeypadEqual as ImGuiKey
declare val ImGuiKey_AppBack as ImGuiKey
declare val ImGuiKey_AppForward as ImGuiKey
declare val ImGuiKey_Oem102 as ImGuiKey
declare val ImGuiKey_GamepadStart as ImGuiKey
declare val ImGuiKey_GamepadBack as ImGuiKey
declare val ImGuiKey_GamepadFaceLeft as ImGuiKey
declare val ImGuiKey_GamepadFaceRight as ImGuiKey
declare val ImGuiKey_GamepadFaceUp as ImGuiKey
declare val ImGuiKey_GamepadFaceDown as ImGuiKey
declare val ImGuiKey_GamepadDpadLeft as ImGuiKey
declare val ImGuiKey_GamepadDpadRight as ImGuiKey
declare val ImGuiKey_GamepadDpadUp as ImGuiKey
declare val ImGuiKey_GamepadDpadDown as ImGuiKey
declare val ImGuiKey_GamepadL1 as ImGuiKey
declare val ImGuiKey_GamepadR1 as ImGuiKey
declare val ImGuiKey_GamepadL2 as ImGuiKey
declare val ImGuiKey_GamepadR2 as ImGuiKey
declare val ImGuiKey_GamepadL3 as ImGuiKey
declare val ImGuiKey_GamepadR3 as ImGuiKey
declare val ImGuiKey_GamepadLStickLeft as ImGuiKey
declare val ImGuiKey_GamepadLStickRight as ImGuiKey
declare val ImGuiKey_GamepadLStickUp as ImGuiKey
declare val ImGuiKey_GamepadLStickDown as ImGuiKey
declare val ImGuiKey_GamepadRStickLeft as ImGuiKey
declare val ImGuiKey_GamepadRStickRight as ImGuiKey
declare val ImGuiKey_GamepadRStickUp as ImGuiKey
declare val ImGuiKey_GamepadRStickDown as ImGuiKey
declare val ImGuiKey_MouseLeft as ImGuiKey
declare val ImGuiKey_MouseRight as ImGuiKey
declare val ImGuiKey_MouseMiddle as ImGuiKey
declare val ImGuiKey_MouseX1 as ImGuiKey
declare val ImGuiKey_MouseX2 as ImGuiKey
declare val ImGuiKey_MouseWheelX as ImGuiKey
declare val ImGuiKey_MouseWheelY as ImGuiKey
declare val ImGuiKey_ReservedForModCtrl as ImGuiKey
declare val ImGuiKey_ReservedForModShift as ImGuiKey
declare val ImGuiKey_ReservedForModAlt as ImGuiKey
declare val ImGuiKey_ReservedForModSuper as ImGuiKey
declare val ImGuiKey_NamedKey_END as ImGuiKey
declare val ImGuiKey_NamedKey_COUNT as ImGuiKey
declare val ImGuiMod_None as ImGuiKey
declare val ImGuiMod_Ctrl as ImGuiKey
declare val ImGuiMod_Shift as ImGuiKey
declare val ImGuiMod_Alt as ImGuiKey
declare val ImGuiMod_Super as ImGuiKey
declare val ImGuiMod_Mask_ as ImGuiKey

declare type ImGuiInputFlags as Int
declare val ImGuiInputFlags_None as ImGuiInputFlags
declare val ImGuiInputFlags_Repeat as ImGuiInputFlags
declare val ImGuiInputFlags_RouteActive as ImGuiInputFlags
declare val ImGuiInputFlags_RouteFocused as ImGuiInputFlags
declare val ImGuiInputFlags_RouteGlobal as ImGuiInputFlags
declare val ImGuiInputFlags_RouteAlways as ImGuiInputFlags
declare val ImGuiInputFlags_RouteOverFocused as ImGuiInputFlags
declare val ImGuiInputFlags_RouteOverActive as ImGuiInputFlags
declare val ImGuiInputFlags_RouteUnlessBgFocused as ImGuiInputFlags
declare val ImGuiInputFlags_RouteFromRootWindow as ImGuiInputFlags
declare val ImGuiInputFlags_Tooltip as ImGuiInputFlags

declare type ImGuiConfigFlags as Int
declare val ImGuiConfigFlags_None as ImGuiConfigFlags
declare val ImGuiConfigFlags_NavEnableKeyboard as ImGuiConfigFlags
declare val ImGuiConfigFlags_NavEnableGamepad as ImGuiConfigFlags
declare val ImGuiConfigFlags_NoMouse as ImGuiConfigFlags
declare val ImGuiConfigFlags_NoMouseCursorChange as ImGuiConfigFlags
declare val ImGuiConfigFlags_NoKeyboard as ImGuiConfigFlags
declare val ImGuiConfigFlags_IsSRGB as ImGuiConfigFlags
declare val ImGuiConfigFlags_IsTouchScreen as ImGuiConfigFlags

declare type ImGuiBackendFlags as Int
declare val ImGuiBackendFlags_None as ImGuiBackendFlags
declare val ImGuiBackendFlags_HasGamepad as ImGuiBackendFlags
declare val ImGuiBackendFlags_HasMouseCursors as ImGuiBackendFlags
declare val ImGuiBackendFlags_HasSetMousePos as ImGuiBackendFlags
declare val ImGuiBackendFlags_RendererHasVtxOffset as ImGuiBackendFlags
declare val ImGuiBackendFlags_RendererHasTextures as ImGuiBackendFlags

declare type ImGuiCol as Int
declare val ImGuiCol_Text as ImGuiCol
declare val ImGuiCol_TextDisabled as ImGuiCol
declare val ImGuiCol_WindowBg as ImGuiCol
declare val ImGuiCol_ChildBg as ImGuiCol
declare val ImGuiCol_PopupBg as ImGuiCol
declare val ImGuiCol_Border as ImGuiCol
declare val ImGuiCol_BorderShadow as ImGuiCol
declare val ImGuiCol_FrameBg as ImGuiCol
declare val ImGuiCol_FrameBgHovered as ImGuiCol
declare val ImGuiCol_FrameBgActive as ImGuiCol
declare val ImGuiCol_TitleBg as ImGuiCol
declare val ImGuiCol_TitleBgActive as ImGuiCol
declare val ImGuiCol_TitleBgCollapsed as ImGuiCol
declare val ImGuiCol_MenuBarBg as ImGuiCol
declare val ImGuiCol_ScrollbarBg as ImGuiCol
declare val ImGuiCol_ScrollbarGrab as ImGuiCol
declare val ImGuiCol_ScrollbarGrabHovered as ImGuiCol
declare val ImGuiCol_ScrollbarGrabActive as ImGuiCol
declare val ImGuiCol_CheckMark as ImGuiCol
declare val ImGuiCol_CheckboxSelectedBg as ImGuiCol
declare val ImGuiCol_SliderGrab as ImGuiCol
declare val ImGuiCol_SliderGrabActive as ImGuiCol
declare val ImGuiCol_Button as ImGuiCol
declare val ImGuiCol_ButtonHovered as ImGuiCol
declare val ImGuiCol_ButtonActive as ImGuiCol
declare val ImGuiCol_Header as ImGuiCol
declare val ImGuiCol_HeaderHovered as ImGuiCol
declare val ImGuiCol_HeaderActive as ImGuiCol
declare val ImGuiCol_Separator as ImGuiCol
declare val ImGuiCol_SeparatorHovered as ImGuiCol
declare val ImGuiCol_SeparatorActive as ImGuiCol
declare val ImGuiCol_ResizeGrip as ImGuiCol
declare val ImGuiCol_ResizeGripHovered as ImGuiCol
declare val ImGuiCol_ResizeGripActive as ImGuiCol
declare val ImGuiCol_InputTextCursor as ImGuiCol
declare val ImGuiCol_TabHovered as ImGuiCol
declare val ImGuiCol_Tab as ImGuiCol
declare val ImGuiCol_TabSelected as ImGuiCol
declare val ImGuiCol_TabSelectedOverline as ImGuiCol
declare val ImGuiCol_TabDimmed as ImGuiCol
declare val ImGuiCol_TabDimmedSelected as ImGuiCol
declare val ImGuiCol_TabDimmedSelectedOverline as ImGuiCol
declare val ImGuiCol_PlotLines as ImGuiCol
declare val ImGuiCol_PlotLinesHovered as ImGuiCol
declare val ImGuiCol_PlotHistogram as ImGuiCol
declare val ImGuiCol_PlotHistogramHovered as ImGuiCol
declare val ImGuiCol_TableHeaderBg as ImGuiCol
declare val ImGuiCol_TableBorderStrong as ImGuiCol
declare val ImGuiCol_TableBorderLight as ImGuiCol
declare val ImGuiCol_TableRowBg as ImGuiCol
declare val ImGuiCol_TableRowBgAlt as ImGuiCol
declare val ImGuiCol_TextLink as ImGuiCol
declare val ImGuiCol_TextSelectedBg as ImGuiCol
declare val ImGuiCol_TreeLines as ImGuiCol
declare val ImGuiCol_DragDropTarget as ImGuiCol
declare val ImGuiCol_DragDropTargetBg as ImGuiCol
declare val ImGuiCol_UnsavedMarker as ImGuiCol
declare val ImGuiCol_NavCursor as ImGuiCol
declare val ImGuiCol_NavWindowingHighlight as ImGuiCol
declare val ImGuiCol_NavWindowingDimBg as ImGuiCol
declare val ImGuiCol_ModalWindowDimBg as ImGuiCol
declare val ImGuiCol_COUNT as ImGuiCol

declare type ImGuiStyleVar as Int
declare val ImGuiStyleVar_Alpha as ImGuiStyleVar
declare val ImGuiStyleVar_DisabledAlpha as ImGuiStyleVar
declare val ImGuiStyleVar_WindowPadding as ImGuiStyleVar
declare val ImGuiStyleVar_WindowRounding as ImGuiStyleVar
declare val ImGuiStyleVar_WindowBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_WindowMinSize as ImGuiStyleVar
declare val ImGuiStyleVar_WindowTitleAlign as ImGuiStyleVar
declare val ImGuiStyleVar_ChildRounding as ImGuiStyleVar
declare val ImGuiStyleVar_ChildBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_PopupRounding as ImGuiStyleVar
declare val ImGuiStyleVar_PopupBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_FramePadding as ImGuiStyleVar
declare val ImGuiStyleVar_FrameRounding as ImGuiStyleVar
declare val ImGuiStyleVar_FrameBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_ItemSpacing as ImGuiStyleVar
declare val ImGuiStyleVar_ItemInnerSpacing as ImGuiStyleVar
declare val ImGuiStyleVar_IndentSpacing as ImGuiStyleVar
declare val ImGuiStyleVar_CellPadding as ImGuiStyleVar
declare val ImGuiStyleVar_ScrollbarSize as ImGuiStyleVar
declare val ImGuiStyleVar_ScrollbarRounding as ImGuiStyleVar
declare val ImGuiStyleVar_ScrollbarPadding as ImGuiStyleVar
declare val ImGuiStyleVar_GrabMinSize as ImGuiStyleVar
declare val ImGuiStyleVar_GrabRounding as ImGuiStyleVar
declare val ImGuiStyleVar_ImageRounding as ImGuiStyleVar
declare val ImGuiStyleVar_ImageBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_TabRounding as ImGuiStyleVar
declare val ImGuiStyleVar_TabBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_TabMinWidthBase as ImGuiStyleVar
declare val ImGuiStyleVar_TabMinWidthShrink as ImGuiStyleVar
declare val ImGuiStyleVar_TabBarBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_TabBarOverlineSize as ImGuiStyleVar
declare val ImGuiStyleVar_TableAngledHeadersAngle as ImGuiStyleVar
declare val ImGuiStyleVar_TableAngledHeadersTextAlign as ImGuiStyleVar
declare val ImGuiStyleVar_TreeLinesSize as ImGuiStyleVar
declare val ImGuiStyleVar_TreeLinesRounding as ImGuiStyleVar
declare val ImGuiStyleVar_MenuItemRounding as ImGuiStyleVar
declare val ImGuiStyleVar_SelectableRounding as ImGuiStyleVar
declare val ImGuiStyleVar_DragDropTargetRounding as ImGuiStyleVar
declare val ImGuiStyleVar_ButtonTextAlign as ImGuiStyleVar
declare val ImGuiStyleVar_SelectableTextAlign as ImGuiStyleVar
declare val ImGuiStyleVar_SeparatorSize as ImGuiStyleVar
declare val ImGuiStyleVar_SeparatorTextBorderSize as ImGuiStyleVar
declare val ImGuiStyleVar_SeparatorTextAlign as ImGuiStyleVar
declare val ImGuiStyleVar_SeparatorTextPadding as ImGuiStyleVar
declare val ImGuiStyleVar_COUNT as ImGuiStyleVar

declare type ImGuiButtonFlags as Int
declare val ImGuiButtonFlags_None as ImGuiButtonFlags
declare val ImGuiButtonFlags_MouseButtonLeft as ImGuiButtonFlags
declare val ImGuiButtonFlags_MouseButtonRight as ImGuiButtonFlags
declare val ImGuiButtonFlags_MouseButtonMiddle as ImGuiButtonFlags
declare val ImGuiButtonFlags_MouseButtonMask_ as ImGuiButtonFlags
declare val ImGuiButtonFlags_EnableNav as ImGuiButtonFlags
declare val ImGuiButtonFlags_AllowOverlap as ImGuiButtonFlags

declare type ImGuiColorEditFlags as Int
declare val ImGuiColorEditFlags_None as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoAlpha as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoPicker as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoOptions as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoSmallPreview as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoInputs as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoTooltip as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoLabel as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoSidePreview as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoDragDrop as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoBorder as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_NoColorMarkers as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_AlphaOpaque as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_AlphaNoBg as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_AlphaPreviewHalf as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_AlphaBar as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_HDR as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_DisplayRGB as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_DisplayHSV as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_DisplayHex as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_Uint8 as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_Float as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_PickerHueBar as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_PickerHueWheel as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_PickerNoRotate as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_InputRGB as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_InputHSV as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_DefaultOptions_ as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_AlphaMask_ as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_DisplayMask_ as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_DataTypeMask_ as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_PickerMask_ as ImGuiColorEditFlags
declare val ImGuiColorEditFlags_InputMask_ as ImGuiColorEditFlags

declare type ImGuiSliderFlags as Int
declare val ImGuiSliderFlags_None as ImGuiSliderFlags
declare val ImGuiSliderFlags_Logarithmic as ImGuiSliderFlags
declare val ImGuiSliderFlags_NoRoundToFormat as ImGuiSliderFlags
declare val ImGuiSliderFlags_NoInput as ImGuiSliderFlags
declare val ImGuiSliderFlags_WrapAround as ImGuiSliderFlags
declare val ImGuiSliderFlags_ClampOnInput as ImGuiSliderFlags
declare val ImGuiSliderFlags_ClampZeroRange as ImGuiSliderFlags
declare val ImGuiSliderFlags_NoSpeedTweaks as ImGuiSliderFlags
declare val ImGuiSliderFlags_ColorMarkers as ImGuiSliderFlags
declare val ImGuiSliderFlags_AlwaysClamp as ImGuiSliderFlags
declare val ImGuiSliderFlags_InvalidMask_ as ImGuiSliderFlags

declare type ImGuiMouseButton as Int
declare val ImGuiMouseButton_Left as ImGuiMouseButton
declare val ImGuiMouseButton_Right as ImGuiMouseButton
declare val ImGuiMouseButton_Middle as ImGuiMouseButton
declare val ImGuiMouseButton_COUNT as ImGuiMouseButton

declare type ImGuiMouseCursor as Int
declare val ImGuiMouseCursor_None as ImGuiMouseCursor
declare val ImGuiMouseCursor_Arrow as ImGuiMouseCursor
declare val ImGuiMouseCursor_TextInput as ImGuiMouseCursor
declare val ImGuiMouseCursor_ResizeAll as ImGuiMouseCursor
declare val ImGuiMouseCursor_ResizeNS as ImGuiMouseCursor
declare val ImGuiMouseCursor_ResizeEW as ImGuiMouseCursor
declare val ImGuiMouseCursor_ResizeNESW as ImGuiMouseCursor
declare val ImGuiMouseCursor_ResizeNWSE as ImGuiMouseCursor
declare val ImGuiMouseCursor_Hand as ImGuiMouseCursor
declare val ImGuiMouseCursor_Wait as ImGuiMouseCursor
declare val ImGuiMouseCursor_Progress as ImGuiMouseCursor
declare val ImGuiMouseCursor_NotAllowed as ImGuiMouseCursor
declare val ImGuiMouseCursor_COUNT as ImGuiMouseCursor

declare type ImGuiMouseSource as Int
declare val ImGuiMouseSource_Mouse as ImGuiMouseSource
declare val ImGuiMouseSource_TouchScreen as ImGuiMouseSource
declare val ImGuiMouseSource_Pen as ImGuiMouseSource
declare val ImGuiMouseSource_COUNT as ImGuiMouseSource

declare type ImGuiCond as Int
declare val ImGuiCond_None as ImGuiCond
declare val ImGuiCond_Always as ImGuiCond
declare val ImGuiCond_Once as ImGuiCond
declare val ImGuiCond_FirstUseEver as ImGuiCond
declare val ImGuiCond_Appearing as ImGuiCond

declare type ImGuiTableFlags as Int
declare val ImGuiTableFlags_None as ImGuiTableFlags
declare val ImGuiTableFlags_Resizable as ImGuiTableFlags
declare val ImGuiTableFlags_Reorderable as ImGuiTableFlags
declare val ImGuiTableFlags_Hideable as ImGuiTableFlags
declare val ImGuiTableFlags_Sortable as ImGuiTableFlags
declare val ImGuiTableFlags_NoSavedSettings as ImGuiTableFlags
declare val ImGuiTableFlags_ContextMenuInBody as ImGuiTableFlags
declare val ImGuiTableFlags_RowBg as ImGuiTableFlags
declare val ImGuiTableFlags_BordersInnerH as ImGuiTableFlags
declare val ImGuiTableFlags_BordersOuterH as ImGuiTableFlags
declare val ImGuiTableFlags_BordersInnerV as ImGuiTableFlags
declare val ImGuiTableFlags_BordersOuterV as ImGuiTableFlags
declare val ImGuiTableFlags_BordersH as ImGuiTableFlags
declare val ImGuiTableFlags_BordersV as ImGuiTableFlags
declare val ImGuiTableFlags_BordersInner as ImGuiTableFlags
declare val ImGuiTableFlags_BordersOuter as ImGuiTableFlags
declare val ImGuiTableFlags_Borders as ImGuiTableFlags
declare val ImGuiTableFlags_NoBordersInBody as ImGuiTableFlags
declare val ImGuiTableFlags_NoBordersInBodyUntilResize as ImGuiTableFlags
declare val ImGuiTableFlags_SizingFixedFit as ImGuiTableFlags
declare val ImGuiTableFlags_SizingFixedSame as ImGuiTableFlags
declare val ImGuiTableFlags_SizingStretchProp as ImGuiTableFlags
declare val ImGuiTableFlags_SizingStretchSame as ImGuiTableFlags
declare val ImGuiTableFlags_NoHostExtendX as ImGuiTableFlags
declare val ImGuiTableFlags_NoHostExtendY as ImGuiTableFlags
declare val ImGuiTableFlags_NoKeepColumnsVisible as ImGuiTableFlags
declare val ImGuiTableFlags_PreciseWidths as ImGuiTableFlags
declare val ImGuiTableFlags_NoClip as ImGuiTableFlags
declare val ImGuiTableFlags_PadOuterX as ImGuiTableFlags
declare val ImGuiTableFlags_NoPadOuterX as ImGuiTableFlags
declare val ImGuiTableFlags_NoPadInnerX as ImGuiTableFlags
declare val ImGuiTableFlags_ScrollX as ImGuiTableFlags
declare val ImGuiTableFlags_ScrollY as ImGuiTableFlags
declare val ImGuiTableFlags_SortMulti as ImGuiTableFlags
declare val ImGuiTableFlags_SortTristate as ImGuiTableFlags
declare val ImGuiTableFlags_HighlightHoveredColumn as ImGuiTableFlags
declare val ImGuiTableFlags_SizingMask_ as ImGuiTableFlags

declare type ImGuiTableColumnFlags as Int
declare val ImGuiTableColumnFlags_None as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_Disabled as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_DefaultHide as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_DefaultSort as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_WidthStretch as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_WidthFixed as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoResize as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoReorder as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoHide as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoClip as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoSort as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoSortAscending as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoSortDescending as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoHeaderLabel as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoHeaderWidth as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_PreferSortAscending as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_PreferSortDescending as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IndentEnable as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IndentDisable as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_AngledHeader as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IsEnabled as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IsVisible as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IsSorted as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IsHovered as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_WidthMask_ as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_IndentMask_ as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_StatusMask_ as ImGuiTableColumnFlags
declare val ImGuiTableColumnFlags_NoDirectResize_ as ImGuiTableColumnFlags

declare type ImGuiTableRowFlags as Int
declare val ImGuiTableRowFlags_None as ImGuiTableRowFlags
declare val ImGuiTableRowFlags_Headers as ImGuiTableRowFlags

declare type ImGuiTableBgTarget as Int
declare val ImGuiTableBgTarget_None as ImGuiTableBgTarget
declare val ImGuiTableBgTarget_RowBg0 as ImGuiTableBgTarget
declare val ImGuiTableBgTarget_RowBg1 as ImGuiTableBgTarget
declare val ImGuiTableBgTarget_CellBg as ImGuiTableBgTarget

declare type ImGuiListClipperFlags as Int
declare val ImGuiListClipperFlags_None as ImGuiListClipperFlags
declare val ImGuiListClipperFlags_NoSetTableRowCounters as ImGuiListClipperFlags

declare type ImGuiMultiSelectFlags as Int
declare val ImGuiMultiSelectFlags_None as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_SingleSelect as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NoSelectAll as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NoRangeSelect as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NoAutoSelect as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NoAutoClear as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NoAutoClearOnReselect as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_BoxSelect1d as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_BoxSelect2d as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_BoxSelectNoScroll as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_ClearOnEscape as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_ClearOnClickVoid as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_ScopeWindow as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_ScopeRect as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_SelectOnAuto as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_SelectOnClickAlways as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_SelectOnClickRelease as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NavWrapX as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_NoSelectOnRightClick as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_SelectOnMask_ as ImGuiMultiSelectFlags
declare val ImGuiMultiSelectFlags_CheckboxMode_ as ImGuiMultiSelectFlags

declare type ImGuiSelectionRequestType as Int
declare val ImGuiSelectionRequestType_None as ImGuiSelectionRequestType
declare val ImGuiSelectionRequestType_SetAll as ImGuiSelectionRequestType
declare val ImGuiSelectionRequestType_SetRange as ImGuiSelectionRequestType

declare type ImDrawFlags as Int
declare val ImDrawFlags_None as ImDrawFlags
declare val ImDrawFlags_RoundTopLeft as ImDrawFlags
declare val ImDrawFlags_RoundTopRight as ImDrawFlags
declare val ImDrawFlags_RoundBottomLeft as ImDrawFlags
declare val ImDrawFlags_RoundBottomRight as ImDrawFlags
declare val ImDrawFlags_RoundNone as ImDrawFlags
declare val ImDrawFlags_RoundAll as ImDrawFlags
declare val ImDrawFlags_RoundTop as ImDrawFlags
declare val ImDrawFlags_RoundBottom as ImDrawFlags
declare val ImDrawFlags_RoundLeft as ImDrawFlags
declare val ImDrawFlags_RoundRight as ImDrawFlags
declare val ImDrawFlags_Closed as ImDrawFlags
declare val ImDrawFlags_JoinMiter as ImDrawFlags
declare val ImDrawFlags_CapSquare as ImDrawFlags
declare val ImDrawFlags_AAFill as ImDrawFlags
declare val ImDrawFlags_AALines as ImDrawFlags
declare val ImDrawFlags_AALineEnds as ImDrawFlags
declare val ImDrawFlags_StrokeInside as ImDrawFlags
declare val ImDrawFlags_StrokeCenter as ImDrawFlags
declare val ImDrawFlags_StrokeCenterBiased as ImDrawFlags
declare val ImDrawFlags_StrokeOutside as ImDrawFlags
declare val ImDrawFlags_StrokeLegacy as ImDrawFlags
declare val ImDrawFlags_TextNoPixelSnap as ImDrawFlags
declare val ImDrawFlags_UseTexForRoundCorners as ImDrawFlags
declare val ImDrawFlags_UseVtxOffset as ImDrawFlags
declare val ImDrawFlags_AllowTexForRoundCorners_ as ImDrawFlags
declare val ImDrawFlags_RoundMask_ as ImDrawFlags
declare val ImDrawFlags_AllowInPushScope_ as ImDrawFlags
declare val ImDrawFlags_AllowInFrameScope_ as ImDrawFlags
declare val ImDrawFlags_StrokeMask_ as ImDrawFlags
declare val ImDrawFlags_InvalidMask_ as ImDrawFlags

declare type ImTextureFormat as Int
declare val ImTextureFormat_RGBA32 as ImTextureFormat
declare val ImTextureFormat_Alpha8 as ImTextureFormat

declare type ImTextureStatus as Int
declare val ImTextureStatus_OK as ImTextureStatus
declare val ImTextureStatus_Destroyed as ImTextureStatus
declare val ImTextureStatus_WantCreate as ImTextureStatus
declare val ImTextureStatus_WantUpdates as ImTextureStatus
declare val ImTextureStatus_WantDestroy as ImTextureStatus

declare type ImFontAtlasFlags as Int
declare val ImFontAtlasFlags_None as ImFontAtlasFlags
declare val ImFontAtlasFlags_NoPowerOfTwoHeight as ImFontAtlasFlags
declare val ImFontAtlasFlags_NoMouseCursors as ImFontAtlasFlags
declare val ImFontAtlasFlags_NoBakedLines as ImFontAtlasFlags
declare val ImFontAtlasFlags_NoBakedRoundCorners as ImFontAtlasFlags

declare type ImFontFlags as Int
declare val ImFontFlags_None as ImFontFlags
declare val ImFontFlags_NoLoadError as ImFontFlags
declare val ImFontFlags_NoLoadGlyphs as ImFontFlags
declare val ImFontFlags_LockBakedSizes as ImFontFlags
declare val ImFontFlags_ImplicitRefSize as ImFontFlags

declare type ImGuiViewportFlags as Int
declare val ImGuiViewportFlags_None as ImGuiViewportFlags
declare val ImGuiViewportFlags_IsPlatformWindow as ImGuiViewportFlags
declare val ImGuiViewportFlags_IsPlatformMonitor as ImGuiViewportFlags
declare val ImGuiViewportFlags_OwnedByApp as ImGuiViewportFlags

// ---------------------------------------------------------------------------------------------------------------------
// Structs
// ---------------------------------------------------------------------------------------------------------------------

declare mut rec ImVec2 as
    x: Float
    y: Float

declare mut rec ImVec4 as
    x: Float
    y: Float
    z: Float
    w: Float

declare mut rec ImTextureRef

declare mut rec ImGuiTableSortSpecs as
    Specs: @ImGuiTableColumnSortSpecs?
    SpecsCount: Int
    SpecsDirty: Bool

declare mut rec ImGuiTableColumnSortSpecs as
    ColumnUserID: ImGuiID
    ColumnIndex: ImS16
    SortOrder: ImS16
    SortDirection: ImGuiSortDirection

declare mut rec ImGuiStyle as
    FontSizeBase: Float
    FontScaleMain: Float
    FontScaleDpi: Float
    Alpha: Float
    DisabledAlpha: Float
    WindowPadding: ImVec2
    WindowRounding: Float
    WindowBorderSize: Float
    WindowBorderHoverPadding: Float
    WindowMinSize: ImVec2
    WindowTitleAlign: ImVec2
    WindowMenuButtonPosition: ImGuiDir
    ChildRounding: Float
    ChildBorderSize: Float
    PopupRounding: Float
    PopupBorderSize: Float
    FramePadding: ImVec2
    FrameRounding: Float
    FrameBorderSize: Float
    ItemSpacing: ImVec2
    ItemInnerSpacing: ImVec2
    CellPadding: ImVec2
    TouchExtraPadding: ImVec2
    IndentSpacing: Float
    ColumnsMinSpacing: Float
    ScrollbarSize: Float
    ScrollbarRounding: Float
    ScrollbarPadding: Float
    GrabMinSize: Float
    GrabRounding: Float
    LogSliderDeadzone: Float
    ImageRounding: Float
    ImageBorderSize: Float
    TabRounding: Float
    TabBorderSize: Float
    TabMinWidthBase: Float
    TabMinWidthShrink: Float
    TabCloseButtonMinWidthSelected: Float
    TabCloseButtonMinWidthUnselected: Float
    TabBarBorderSize: Float
    TabBarOverlineSize: Float
    TableAngledHeadersAngle: Float
    TableAngledHeadersTextAlign: ImVec2
    TreeLinesFlags: ImGuiTreeNodeFlags
    TreeLinesSize: Float
    TreeLinesRounding: Float
    MenuItemRounding: Float
    SelectableRounding: Float
    DragDropTargetRounding: Float
    DragDropTargetBorderSize: Float
    DragDropTargetPadding: Float
    ColorMarkerSize: Float
    ColorButtonPosition: ImGuiDir
    ButtonTextAlign: ImVec2
    SelectableTextAlign: ImVec2
    InputTextCursorSize: Float
    SeparatorSize: Float
    SeparatorTextBorderSize: Float
    SeparatorTextAlign: ImVec2
    SeparatorTextPadding: ImVec2
    DisplayWindowPadding: ImVec2
    DisplaySafeAreaPadding: ImVec2
    MouseCursorScale: Float
    AntiAliasedLines: Bool
    AntiAliasedLineEnds: Bool
    AntiAliasedFill: Bool
    CurveTessellationMaxError: Float
    CircleTessellationMaxError: Float
    Colors: ImVec4[61]
    HoverStationaryDelay: Float
    HoverDelayShort: Float
    HoverDelayNormal: Float
    HoverFlagsForTooltipMouse: ImGuiHoveredFlags
    HoverFlagsForTooltipNav: ImGuiHoveredFlags

declare mut rec ImGuiKeyData as
    Down: Bool
    DownDuration: Float
    DownDurationPrev: Float
    AnalogValue: Float

declare mut rec ImGuiIO as
    ConfigFlags: ImGuiConfigFlags
    BackendFlags: ImGuiBackendFlags
    DisplaySize: ImVec2
    DisplayFramebufferScale: ImVec2
    DeltaTime: Float
    IniSavingRate: Float
    IniFilename: CString?
    LogFilename: CString?
    UserData: @Any?
    Fonts: @ImFontAtlas
    FontDefault: @ImFont?
    FontAllowUserScaling: Bool
    ConfigNavSwapGamepadButtons: Bool
    ConfigNavMoveSetMousePos: Bool
    ConfigNavCaptureKeyboard: Bool
    ConfigNavEscapeClearFocusItem: Bool
    ConfigNavEscapeClearFocusWindow: Bool
    ConfigNavCursorVisibleAuto: Bool
    ConfigNavCursorVisibleAlways: Bool
    ConfigMacOSXBehaviors: Bool
    ConfigInputTrickleEventQueue: Bool
    ConfigInputTextCursorBlink: Bool
    ConfigInputTextEnterKeepActive: Bool
    ConfigColorEditFlags: ImGuiColorEditFlags
    ConfigDragClickToInputText: Bool
    ConfigWindowsResizeFromEdges: Bool
    ConfigWindowsMoveFromTitleBarOnly: Bool
    ConfigWindowsCopyContentsWithCtrlC: Bool
    ConfigScrollbarScrollByPage: Bool
    ConfigIniSettingsSaveLastUsedDate: Bool
    ConfigIniSettingsAutoDiscardMonths: Int
    ConfigDebugIniSettings: Bool
    MouseDrawCursor: Bool
    ConfigMemoryCompactTimer: Float
    MouseDoubleClickTime: Float
    MouseDoubleClickMaxDist: Float
    MouseSingleClickDelay: Float
    MouseDragThreshold: Float
    KeyRepeatDelay: Float
    KeyRepeatRate: Float
    ConfigErrorRecovery: Bool
    ConfigErrorRecoveryEnableAssert: Bool
    ConfigErrorRecoveryEnableDebugLog: Bool
    ConfigErrorRecoveryEnableTooltip: Bool
    ConfigDebugIsDebuggerPresent: Bool
    ConfigDebugHighlightIdConflicts: Bool
    ConfigDebugHighlightIdConflictsShowItemPicker: Bool
    ConfigDebugBeginReturnValueOnce: Bool
    ConfigDebugBeginReturnValueLoop: Bool
    ConfigDebugIgnoreFocusLoss: Bool
    ConfigDebugDrawListDefaultsToStrokeLegacy: Bool
    BackendPlatformName: CString?
    BackendRendererName: CString?
    BackendPlatformUserData: @Any?
    BackendRendererUserData: @Any?
    BackendLanguageUserData: @Any?
    WantCaptureMouse: Bool
    WantCaptureKeyboard: Bool
    WantTextInput: Bool
    WantSetMousePos: Bool
    WantSaveIniSettings: Bool
    NavActive: Bool
    NavVisible: Bool
    Framerate: Float
    MetricsRenderVertices: Int
    MetricsRenderIndices: Int
    MetricsRenderWindows: Int
    MetricsActiveWindows: Int
    MouseDelta: ImVec2
    Ctx: @ImGuiContext?
    MousePos: ImVec2
    MouseDown: Bool[5]
    MouseWheel: Float
    MouseWheelH: Float
    MouseSource: ImGuiMouseSource
    KeyCtrl: Bool
    KeyShift: Bool
    KeyAlt: Bool
    KeySuper: Bool
    KeyMods: ImGuiKeyChord
    KeysData: ImGuiKeyData[155]
    WantCaptureMouseUnlessPopupClose: Bool
    MousePosPrev: ImVec2
    MouseClickedPos: ImVec2[5]
    MouseClickedTime: Double[5]
    MouseClicked: Bool[5]
    MouseDoubleClicked: Bool[5]
    MouseClickedCount: ImU16[5]
    MouseClickedLastCount: ImU16[5]
    MouseReleased: Bool[5]
    MouseReleasedTime: Double[5]
    MouseDownOwned: Bool[5]
    MouseDownOwnedUnlessPopupClose: Bool[5]
    MouseWheelRequestAxisSwap: Bool
    MouseCtrlLeftAsRightClick: Bool
    MouseDownDuration: Float[5]
    MouseDownDurationPrev: Float[5]
    MouseDragMaxDistanceSqr: Float[5]
    PenPressure: Float
    AppFocusLost: Bool
    AppAcceptingEvents: Bool
    InputQueueSurrogate: ImWchar16
    InputQueueCharacters: ImVector_ImWchar

declare mut rec ImGuiInputTextCallbackData as
    Ctx: @ImGuiContext?
    EventFlag: ImGuiInputTextFlags
    Flags: ImGuiInputTextFlags
    UserData: @Any?
    ID: ImGuiID
    EventKey: ImGuiKey
    EventChar: ImWchar
    EventActivated: Bool
    BufDirty: Bool
    Buf: CString?
    BufTextLen: Int
    BufSize: Int
    CursorPos: Int
    SelectionStart: Int
    SelectionEnd: Int

declare mut rec ImGuiSizeCallbackData as
    UserData: @Any?
    Pos: ImVec2
    CurrentSize: ImVec2
    DesiredSize: ImVec2

declare mut rec ImGuiPayload as
    Data: @Any?
    DataSize: Int
    SourceId: ImGuiID
    SourceParentId: ImGuiID
    DataFrameCount: Int
    DataType: Char[33]
    Preview: Bool
    Delivery: Bool

declare mut rec ImGuiOnceUponAFrame as
    RefFrame: Int

declare mut rec ImGuiTextFilter as
    InputBuf: Char[256]

declare mut rec ImGuiTextBuffer as
    Buf: ImVector_char

declare mut rec ImGuiStoragePair as
    key: ImGuiID

declare mut rec ImGuiStorage as
    Data: ImVector_ImGuiStoragePair

declare mut rec ImGuiListClipper as
    DisplayStart: Int
    DisplayEnd: Int
    UserIndex: Int
    ItemsCount: Int
    ItemsHeight: Float
    Flags: ImGuiListClipperFlags
    StartPosY: Double
    StartSeekOffsetY: Double
    Ctx: @ImGuiContext?
    TempData: @Any?

declare mut rec ImColor as
    Value: ImVec4

declare mut rec ImGuiMultiSelectIO as
    Requests: ImVector_ImGuiSelectionRequest
    RangeSrcItem: ImGuiSelectionUserData
    NavIdItem: ImGuiSelectionUserData
    NavIdSelected: Bool
    RangeSrcReset: Bool
    ItemsCount: Int

declare mut rec ImGuiSelectionRequest as
    Type: ImGuiSelectionRequestType
    Selected: Bool
    RangeDirection: ImS8
    RangeFirstItem: ImGuiSelectionUserData
    RangeLastItem: ImGuiSelectionUserData

declare mut rec ImGuiSelectionBasicStorage as
    Size: Int
    PreserveOrder: Bool
    UserData: @Any?
    AdapterIndexToStorageId: ((self: @ImGuiSelectionBasicStorage?, idx: Int) => ImGuiID)?

declare mut rec ImGuiSelectionExternalStorage as
    UserData: @Any?
    AdapterSetItemSelected: ((self: @ImGuiSelectionExternalStorage?, idx: Int, selected: Bool) => None)?

declare mut rec ImDrawCmd as
    ClipRect: ImVec4
    TexRef: ImTextureRef
    VtxOffset: UInt
    IdxOffset: UInt
    ElemCount: UInt
    UserCallback: ImDrawCallback?
    UserCallbackData: @Any?
    UserCallbackDataSize: Int
    UserCallbackDataOffset: Int

declare mut rec ImDrawVert as
    pos: ImVec2
    uv: ImVec2
    col: ImU32

declare mut rec ImDrawCmdHeader as
    ClipRect: ImVec4
    TexRef: ImTextureRef
    VtxOffset: UInt

declare mut rec ImDrawChannel

declare mut rec ImDrawListSplitter

declare mut rec ImDrawList as
    CmdBuffer: ImVector_ImDrawCmd
    IdxBuffer: ImVector_ImDrawIdx
    VtxBuffer: ImVector_ImDrawVert
    Flags: ImDrawFlags

declare mut rec ImDrawData as
    Valid: Bool
    FrameCount: Int
    TotalIdxCount: Int
    TotalVtxCount: Int
    CmdLists: ImVector_ImDrawListPtr
    DisplayPos: ImVec2
    DisplaySize: ImVec2
    FramebufferScale: ImVec2
    OwnerViewport: @ImGuiViewport?
    Textures: @ImVector_ImTextureDataPtr?

declare mut rec ImTextureRect as
    x: UShort
    y: UShort
    w: UShort
    h: UShort

declare mut rec ImTextureData as
    UniqueID: Int
    Status: ImTextureStatus
    BackendUserData: @Any?
    QueueUserData: @Any?
    TexID: ImTextureID
    Format: ImTextureFormat
    Width: Int
    Height: Int
    BytesPerPixel: Int
    Pixels: UChar[]c?
    UsedRect: ImTextureRect
    UpdateRect: ImTextureRect
    Updates: ImVector_ImTextureRect
    UnusedFrames: Int
    RefCount: UShort
    UseColors: Bool
    WantDestroyNextFrame: Bool

declare mut rec ImFontConfig as
    Name: Char[40]
    FontData: @Any?
    FontDataSize: Int
    FontDataOwnedByAtlas: Bool
    MergeMode: Bool
    PixelSnapH: Bool
    OversampleH: ImS8
    OversampleV: ImS8
    EllipsisChar: ImWchar
    SizePixels: Float
    GlyphRanges: ImWchar[]c?
    GlyphExcludeRanges: ImWchar[]c?
    GlyphOffset: ImVec2
    GlyphMinAdvanceX: Float
    GlyphMaxAdvanceX: Float
    GlyphExtraAdvanceX: Float
    FontNo: ImU32
    FontLoaderFlags: UInt
    RasterizerMultiply: Float
    RasterizerDensity: Float
    ExtraSizeScale: Float
    Flags: ImFontFlags
    DstFont: @ImFont?
    FontLoader: @ImFontLoader?
    FontLoaderData: @Any?

declare mut rec ImFontGlyph as
    Colored: UInt : 1
    Visible: UInt : 1
    SourceIdx: UInt : 4
    Codepoint: UInt : 26
    AdvanceX: Float
    X0: Float
    Y0: Float
    X1: Float
    Y1: Float
    U0: Float
    V0: Float
    U1: Float
    V1: Float
    PackId: Int

declare mut rec ImFontGlyphRangesBuilder as
    UsedChars: ImVector_ImU32

declare mut rec ImFontAtlasRect as
    x: UShort
    y: UShort
    w: UShort
    h: UShort
    uv0: ImVec2
    uv1: ImVec2

declare mut rec ImFontAtlas as
    Flags: ImFontAtlasFlags
    TexDesiredFormat: ImTextureFormat
    TexGlyphPadding: Int
    TexMinWidth: Int
    TexMinHeight: Int
    TexMaxWidth: Int
    TexMaxHeight: Int
    UserData: @Any?
    TexRef: ImTextureRef
    TexData: @ImTextureData?
    TexList: ImVector_ImTextureDataPtr
    Locked: Bool
    RendererHasTextures: Bool
    TexIsBuilt: Bool
    TexPixelsUseColors: Bool
    TexUvScale: ImVec2
    TexUvWhitePixel: ImVec2
    Fonts: ImVector_ImFontPtr
    Sources: ImVector_ImFontConfig
    TexNextUniqueID: Int
    FontNextUniqueID: Int
    DrawListSharedDatas: ImVector_ImDrawListSharedDataPtr
    Builder: @ImFontAtlasBuilder?
    FontLoader: @ImFontLoader?
    FontLoaderName: CString?
    FontLoaderData: @Any?
    FontLoaderFlags: UInt
    RefCount: Int
    OwnerContext: @ImGuiContext

declare mut rec ImFontBaked as
    IndexAdvanceX: ImVector_float
    FallbackAdvanceX: Float
    Size: Float
    RasterizerDensity: Float
    IndexLookup: ImVector_ImU16
    Glyphs: ImVector_ImFontGlyph
    FallbackGlyphIndex: Int
    Ascent: Float
    Descent: Float
    MetricsTotalSurface: UInt : 26
    WantDestroy: UInt : 1
    LoadNoFallback: UInt : 1
    LoadNoRenderOnLayout: UInt : 1
    LastUsedFrame: Int
    BakedId: ImGuiID
    OwnerFont: @ImFont
    FontLoaderDatas: @Any?

declare mut rec ImFont as
    LastBaked: @ImFontBaked?
    OwnerAtlas: @ImFontAtlas?
    Flags: ImFontFlags
    CurrentRasterizerDensity: Float
    FontId: ImGuiID
    LegacySize: Float
    Sources: ImVector_ImFontConfigPtr
    EllipsisChar: ImWchar
    FallbackChar: ImWchar
    Used8kPagesMap: ImU8[1]
    EllipsisAutoBake: Bool
    RemapPairs: ImGuiStorage

declare mut rec ImGuiViewport as
    ID: ImGuiID
    Flags: ImGuiViewportFlags
    Pos: ImVec2
    Size: ImVec2
    FramebufferScale: ImVec2
    WorkPos: ImVec2
    WorkSize: ImVec2
    PlatformHandle: @Any?
    PlatformHandleRaw: @Any?

declare mut rec ImGuiPlatformIO as
    Platform_GetClipboardTextFn: ((ctx: @ImGuiContext?) => CString?)?
    Platform_SetClipboardTextFn: ((ctx: @ImGuiContext?, text: CString?) => None)?
    Platform_ClipboardUserData: @Any?
    Platform_OpenInShellFn: ((ctx: @ImGuiContext?, path: CString?) => Bool)?
    Platform_OpenInShellUserData: @Any?
    Platform_SetImeDataFn: ((ctx: @ImGuiContext?, viewport: @ImGuiViewport?, data: @ImGuiPlatformImeData?) => None)?
    Platform_ImeUserData: @Any?
    Platform_LocaleDecimalPoint: ImWchar
    Platform_SessionDate: Int
    Renderer_TextureMaxWidth: Int
    Renderer_TextureMaxHeight: Int
    Renderer_RenderState: @Any?
    DrawCallback_ResetRenderState: ImDrawCallback?
    DrawCallback_SetSamplerLinear: ImDrawCallback?
    DrawCallback_SetSamplerNearest: ImDrawCallback?
    DrawCallback_SetSamplerFromTex: ImDrawCallback?
    Textures: ImVector_ImTextureDataPtr

declare mut rec ImGuiPlatformImeData as
    WantVisible: Bool
    WantTextInput: Bool
    InputPos: ImVec2
    InputLineHeight: Float
    ViewportId: ImGuiID

// ---------------------------------------------------------------------------------------------------------------------
// Functions, in the order of imgui.h
// ---------------------------------------------------------------------------------------------------------------------
/** A new ImVec2 with ImGui's defaults, allocated by ImGui; free it with ImVec2_destroy. */
declare fn ImVec2_ImVec2_Nil as (): @ImVec2
/** Frees a ImVec2 made by its constructor. */
declare fn ImVec2_destroy as (self: @ImVec2)
/** A new ImVec2 with ImGui's defaults, allocated by ImGui; free it with ImVec2_destroy. */
declare fn ImVec2_ImVec2_Float as (_x: Float, _y: Float): @ImVec2
/** A new ImVec4 with ImGui's defaults, allocated by ImGui; free it with ImVec4_destroy. */
declare fn ImVec4_ImVec4_Nil as (): @ImVec4
/** Frees a ImVec4 made by its constructor. */
declare fn ImVec4_destroy as (self: @ImVec4)
/** A new ImVec4 with ImGui's defaults, allocated by ImGui; free it with ImVec4_destroy. */
declare fn ImVec4_ImVec4_Float as (_x: Float, _y: Float, _z: Float, _w: Float): @ImVec4
/** A new ImTextureRef with ImGui's defaults, allocated by ImGui; free it with ImTextureRef_destroy. */
declare fn ImTextureRef_ImTextureRef_Nil as (): @ImTextureRef
/** Frees a ImTextureRef made by its constructor. */
declare fn ImTextureRef_destroy as (self: @ImTextureRef)
/** A new ImTextureRef with ImGui's defaults, allocated by ImGui; free it with ImTextureRef_destroy. */
declare fn ImTextureRef_ImTextureRef_TextureID as (tex_id: ImTextureID): @ImTextureRef
/** == (_TexData ? _TexData->TexID : _TexID) // Implemented below in the file. */
declare fn ImTextureRef_GetTexID as (self: @ImTextureRef): ImTextureID
declare fn igCreateContext as (shared_font_atlas: @ImFontAtlas? = null): @ImGuiContext
/** NULL = destroy current context */
declare fn igDestroyContext as (ctx: @ImGuiContext? = null)
declare fn igGetCurrentContext as (): @ImGuiContext?
declare fn igSetCurrentContext as (ctx: @ImGuiContext)
/** access the ImGuiIO structure (mouse/keyboard/gamepad inputs, time, various configuration options/flags) */
declare fn igGetIO_Nil as (): @ImGuiIO
declare fn igGetIO extern "igGetIO_Nil" as (): @ImGuiIO
/** access the ImGuiPlatformIO structure (mostly hooks/functions to connect to platform/renderer and OS Clipboard, IME etc.) */
declare fn igGetPlatformIO_Nil as (): @ImGuiPlatformIO
declare fn igGetPlatformIO extern "igGetPlatformIO_Nil" as (): @ImGuiPlatformIO
/** access the Style structure (colors, sizes). Always use PushStyleColor(), PushStyleVar() to modify style mid-frame! */
declare fn igGetStyle as (): @ImGuiStyle
/** start a new Dear ImGui frame, you can submit any command from this point until Render()/EndFrame(). */
declare fn igNewFrame as ()
/** ends the Dear ImGui frame. automatically called by Render(). If you don't need to render data (skipping rendering) you may call EndFrame() without Render()... but you'll have wasted CPU already! If you don't need to render, better to not create any windows and not call NewFrame() at all! */
declare fn igEndFrame as ()
/** ends the Dear ImGui frame, finalize the draw data. You can then get call GetDrawData(). */
declare fn igRender as ()
/** valid after Render() and until the next call to NewFrame(). Call ImGui_ImplXXXX_RenderDrawData() function in your Renderer Backend to render. */
declare fn igGetDrawData as (): @ImDrawData?
/** create Demo window. demonstrate most ImGui features. call this to learn about the library! try to make it always available in your application! */
declare fn igShowDemoWindow as (p_open: @Bool? = null)
/** create Metrics/Debugger window. display Dear ImGui internals: windows, draw commands, various internal state, etc. */
declare fn igShowMetricsWindow as (p_open: @Bool? = null)
/** create Debug Log window. display a simplified log of important dear imgui events. */
declare fn igShowDebugLogWindow as (p_open: @Bool? = null)
/** create Stack Tool window. hover items with mouse to query information about the source of their unique ID. */
declare fn igShowIDStackToolWindow as (p_open: @Bool? = null)
/** create About window. display Dear ImGui version, credits and build/system information. */
declare fn igShowAboutWindow as (p_open: @Bool? = null)
/** add style editor block (not a window). you can pass in a reference ImGuiStyle structure to compare to, revert to and save to (else it uses the default style) */
declare fn igShowStyleEditor as (ref: @ImGuiStyle? = null)
/** add style selector block (not a window), essentially a combo listing the default styles. */
declare fn igShowStyleSelector as (label: CString): Bool
/** add font selector block (not a window), essentially a combo listing the loaded fonts. */
declare fn igShowFontSelector as (label: CString)
/** add basic help/info block (not a window): how to manipulate ImGui as an end-user (mouse/keyboard controls). */
declare fn igShowUserGuide as ()
/** get the compiled version string e.g. "1.80 WIP" (essentially the value for IMGUI_VERSION from the compiled version of imgui.cpp) */
declare fn igGetVersion as (): CString
/** new, recommended style (default) */
declare fn igStyleColorsDark as (dst: @ImGuiStyle? = null)
/** best used with borders and a custom, thicker font */
declare fn igStyleColorsLight as (dst: @ImGuiStyle? = null)
/** classic imgui style */
declare fn igStyleColorsClassic as (dst: @ImGuiStyle? = null)
declare fn igBegin as (name: CString, p_open: @Bool? = null, flags: ImGuiWindowFlags = 0): Bool
declare fn igEnd as ()
declare fn igBeginChild_Str as (str_id: CString, size: ImVec2 = ImVec2 { 0.0f, 0.0f }, child_flags: ImGuiChildFlags = 0, window_flags: ImGuiWindowFlags = 0): Bool
declare fn igBeginChild extern "igBeginChild_Str" as (str_id: CString, size: ImVec2 = ImVec2 { 0.0f, 0.0f }, child_flags: ImGuiChildFlags = 0, window_flags: ImGuiWindowFlags = 0): Bool
declare fn igBeginChild_ID as (id: ImGuiID, size: ImVec2 = ImVec2 { 0.0f, 0.0f }, child_flags: ImGuiChildFlags = 0, window_flags: ImGuiWindowFlags = 0): Bool
declare fn igBeginChild extern "igBeginChild_ID" as (id: ImGuiID, size: ImVec2 = ImVec2 { 0.0f, 0.0f }, child_flags: ImGuiChildFlags = 0, window_flags: ImGuiWindowFlags = 0): Bool
declare fn igEndChild as ()
declare fn igIsWindowAppearing as (): Bool
declare fn igIsWindowCollapsed as (): Bool
/** is current window focused? or its root/child, depending on flags. see flags for options. */
declare fn igIsWindowFocused as (flags: ImGuiFocusedFlags = 0): Bool
/** is current window hovered and hoverable (e.g. not blocked by a popup/modal)? See ImGuiHoveredFlags_ for options. IMPORTANT: If you are trying to check whether your mouse should be dispatched to Dear ImGui or to your underlying app, you should not use this function! Use the 'io.WantCaptureMouse' boolean for that! Refer to FAQ entry "How can I tell whether to dispatch mouse/keyboard to Dear ImGui or my application?" for details. */
declare fn igIsWindowHovered as (flags: ImGuiHoveredFlags = 0): Bool
/** get draw list associated to the current window, to append your own drawing primitives */
declare fn igGetWindowDrawList as (): @ImDrawList
/** get current window position in screen space (IT IS UNLIKELY YOU EVER NEED TO USE THIS. Consider always using GetCursorScreenPos() and GetContentRegionAvail() instead) */
declare fn igGetWindowPos as (): ImVec2
/** get current window size (IT IS UNLIKELY YOU EVER NEED TO USE THIS. Consider always using GetCursorScreenPos() and GetContentRegionAvail() instead) */
declare fn igGetWindowSize as (): ImVec2
/** get current window width (IT IS UNLIKELY YOU EVER NEED TO USE THIS). Shortcut for GetWindowSize().x. */
declare fn igGetWindowWidth as (): Float
/** get current window height (IT IS UNLIKELY YOU EVER NEED TO USE THIS). Shortcut for GetWindowSize().y. */
declare fn igGetWindowHeight as (): Float
/** set next window position. call before Begin(). use pivot=(0.5f,0.5f) to center on given point, etc. */
declare fn igSetNextWindowPos as (pos: ImVec2, cond: ImGuiCond = 0, pivot: ImVec2 = ImVec2 { 0.0f, 0.0f })
/** set next window size. set axis to 0.0f to force an auto-fit on this axis. call before Begin() */
declare fn igSetNextWindowSize as (size: ImVec2, cond: ImGuiCond = 0)
/** set next window size limits. use 0.0f or FLT_MAX if you don't want limits. Use -1 for both min and max of same axis to preserve current size (which itself is a constraint). Use callback to apply non-trivial programmatic constraints. */
declare fn igSetNextWindowSizeConstraints as (size_min: ImVec2, size_max: ImVec2, custom_callback: ImGuiSizeCallback? = null, custom_callback_data: @Any? = null)
/** set next window content size (~ scrollable client area, which enforce the range of scrollbars). Not including window decorations (title bar, menu bar, etc.) nor WindowPadding. set an axis to 0.0f to leave it automatic. call before Begin() */
declare fn igSetNextWindowContentSize as (size: ImVec2)
/** set next window collapsed state. call before Begin() */
declare fn igSetNextWindowCollapsed as (collapsed: Bool, cond: ImGuiCond = 0)
/** set next window to be focused / top-most. call before Begin() */
declare fn igSetNextWindowFocus as ()
/** set next window scrolling value (use < 0.0f to not affect a given axis). */
declare fn igSetNextWindowScroll as (scroll: ImVec2)
/** set next window background color alpha. helper to easily override the Alpha component of ImGuiCol_WindowBg/ChildBg/PopupBg. you may also use ImGuiWindowFlags_NoBackground. */
declare fn igSetNextWindowBgAlpha as (alpha: Float)
/** (not recommended) set current window position - call within Begin()/End(). prefer using SetNextWindowPos(), as this may incur tearing and side-effects. */
declare fn igSetWindowPos_Vec2 as (pos: ImVec2, cond: ImGuiCond = 0)
declare fn igSetWindowPos extern "igSetWindowPos_Vec2" as (pos: ImVec2, cond: ImGuiCond = 0)
/** (not recommended) set current window size - call within Begin()/End(). set to ImVec2(0, 0) to force an auto-fit. prefer using SetNextWindowSize(), as this may incur tearing and minor side-effects. */
declare fn igSetWindowSize_Vec2 as (size: ImVec2, cond: ImGuiCond = 0)
declare fn igSetWindowSize extern "igSetWindowSize_Vec2" as (size: ImVec2, cond: ImGuiCond = 0)
/** (not recommended) set current window collapsed state. prefer using SetNextWindowCollapsed(). */
declare fn igSetWindowCollapsed_Bool as (collapsed: Bool, cond: ImGuiCond = 0)
declare fn igSetWindowCollapsed extern "igSetWindowCollapsed_Bool" as (collapsed: Bool, cond: ImGuiCond = 0)
/** (not recommended) set current window to be focused / top-most. prefer using SetNextWindowFocus(). */
declare fn igSetWindowFocus_Nil as ()
declare fn igSetWindowFocus extern "igSetWindowFocus_Nil" as ()
/** set named window position. */
declare fn igSetWindowPos_Str as (name: CString, pos: ImVec2, cond: ImGuiCond = 0)
declare fn igSetWindowPos extern "igSetWindowPos_Str" as (name: CString, pos: ImVec2, cond: ImGuiCond = 0)
/** set named window size. set axis to 0.0f to force an auto-fit on this axis. */
declare fn igSetWindowSize_Str as (name: CString, size: ImVec2, cond: ImGuiCond = 0)
declare fn igSetWindowSize extern "igSetWindowSize_Str" as (name: CString, size: ImVec2, cond: ImGuiCond = 0)
/** set named window collapsed state */
declare fn igSetWindowCollapsed_Str as (name: CString, collapsed: Bool, cond: ImGuiCond = 0)
declare fn igSetWindowCollapsed extern "igSetWindowCollapsed_Str" as (name: CString, collapsed: Bool, cond: ImGuiCond = 0)
/** set named window to be focused / top-most. use NULL to remove focus. */
declare fn igSetWindowFocus_Str as (name: CString)
declare fn igSetWindowFocus extern "igSetWindowFocus_Str" as (name: CString)
/** get scrolling amount [0 .. GetScrollMaxX()] */
declare fn igGetScrollX as (): Float
/** get scrolling amount [0 .. GetScrollMaxY()] */
declare fn igGetScrollY as (): Float
/** set scrolling amount [0 .. GetScrollMaxX()] */
declare fn igSetScrollX_Float as (scroll_x: Float)
declare fn igSetScrollX extern "igSetScrollX_Float" as (scroll_x: Float)
/** set scrolling amount [0 .. GetScrollMaxY()] */
declare fn igSetScrollY_Float as (scroll_y: Float)
declare fn igSetScrollY extern "igSetScrollY_Float" as (scroll_y: Float)
/** get maximum scrolling amount ~~ ContentSize.x - WindowSize.x - DecorationsSize.x */
declare fn igGetScrollMaxX as (): Float
/** get maximum scrolling amount ~~ ContentSize.y - WindowSize.y - DecorationsSize.y */
declare fn igGetScrollMaxY as (): Float
/** adjust scrolling amount to make current cursor position visible. center_x_ratio=0.0: left, 0.5: center, 1.0: right. When using to make a "default/current item" visible, consider using SetItemDefaultFocus() instead. */
declare fn igSetScrollHereX as (center_x_ratio: Float = 0.5f)
/** adjust scrolling amount to make current cursor position visible. center_y_ratio=0.0: top, 0.5: center, 1.0: bottom. When using to make a "default/current item" visible, consider using SetItemDefaultFocus() instead. */
declare fn igSetScrollHereY as (center_y_ratio: Float = 0.5f)
/** adjust scrolling amount to make given position visible. Generally GetCursorStartPos() + offset to compute a valid position. */
declare fn igSetScrollFromPosX_Float as (local_x: Float, center_x_ratio: Float = 0.5f)
declare fn igSetScrollFromPosX extern "igSetScrollFromPosX_Float" as (local_x: Float, center_x_ratio: Float = 0.5f)
/** adjust scrolling amount to make given position visible. Generally GetCursorStartPos() + offset to compute a valid position. */
declare fn igSetScrollFromPosY_Float as (local_y: Float, center_y_ratio: Float = 0.5f)
declare fn igSetScrollFromPosY extern "igSetScrollFromPosY_Float" as (local_y: Float, center_y_ratio: Float = 0.5f)
/** Use NULL as a shortcut to keep current font. Use 0.0f to keep current size. */
declare fn igPushFont as (font: @ImFont, font_size_base_unscaled: Float)
declare fn igPopFont as ()
/** get current font */
declare fn igGetFont as (): @ImFont
/** get current scaled font size (= height in pixels). AFTER global scale factors applied. *IMPORTANT* DO NOT PASS THIS VALUE TO PushFont()! Use ImGui::GetStyle().FontSizeBase to get value before global scale factors. */
declare fn igGetFontSize as (): Float
/** get current font bound at current size // == GetFont()->GetFontBaked(GetFontSize()) */
declare fn igGetFontBaked as (): @ImFontBaked
/** modify a style color. always use this if you modify the style after NewFrame(). */
declare fn igPushStyleColor_U32 as (idx: ImGuiCol, col: ImU32)
declare fn igPushStyleColor extern "igPushStyleColor_U32" as (idx: ImGuiCol, col: ImU32)
declare fn igPushStyleColor_Vec4 as (idx: ImGuiCol, col: ImVec4)
declare fn igPushStyleColor extern "igPushStyleColor_Vec4" as (idx: ImGuiCol, col: ImVec4)
declare fn igPopStyleColor as (count: Int = 1)
/** modify a style float variable. always use this if you modify the style after NewFrame()! */
declare fn igPushStyleVar_Float as (idx: ImGuiStyleVar, val_: Float)
declare fn igPushStyleVar extern "igPushStyleVar_Float" as (idx: ImGuiStyleVar, val_: Float)
/** modify a style ImVec2 variable. " */
declare fn igPushStyleVar_Vec2 as (idx: ImGuiStyleVar, val_: ImVec2)
declare fn igPushStyleVar extern "igPushStyleVar_Vec2" as (idx: ImGuiStyleVar, val_: ImVec2)
/** modify X component of a style ImVec2 variable. " */
declare fn igPushStyleVarX as (idx: ImGuiStyleVar, val_x: Float)
/** modify Y component of a style ImVec2 variable. " */
declare fn igPushStyleVarY as (idx: ImGuiStyleVar, val_y: Float)
declare fn igPopStyleVar as (count: Int = 1)
/** modify specified shared item flag, e.g. PushItemFlag(ImGuiItemFlags_NoTabStop, true) */
declare fn igPushItemFlag as (option: ImGuiItemFlags, enabled: Bool)
declare fn igPopItemFlag as ()
/** push width of items for common large "item+label" widgets. >0.0f: width in pixels, <0.0f align xx pixels to the right of window (so -FLT_MIN always align width to the right side). */
declare fn igPushItemWidth as (item_width: Float)
declare fn igPopItemWidth as ()
/** set width of the _next_ common large "item+label" widget. >0.0f: width in pixels, <0.0f align xx pixels to the right of window (so -FLT_MIN always align width to the right side) */
declare fn igSetNextItemWidth as (item_width: Float)
/** width of item given pushed settings and current cursor position. NOT necessarily the width of last item unlike most 'Item' functions. */
declare fn igCalcItemWidth as (): Float
/** push word-wrapping position for Text*() commands. < 0.0f: no wrapping; 0.0f: wrap to end of window (or column); > 0.0f: wrap at 'wrap_pos_x' position in window local space */
declare fn igPushTextWrapPos as (wrap_local_pos_x: Float = 0.0f)
declare fn igPopTextWrapPos as ()
/** get UV coordinate for a white pixel, useful to draw custom shapes via the ImDrawList API */
declare fn igGetFontTexUvWhitePixel as (): ImVec2
/** retrieve given style color with style alpha applied and optional extra alpha multiplier, packed as a 32-bit value suitable for ImDrawList */
declare fn igGetColorU32_Col as (idx: ImGuiCol, alpha_mul: Float = 1.0f): ImU32
declare fn igGetColorU32 extern "igGetColorU32_Col" as (idx: ImGuiCol, alpha_mul: Float = 1.0f): ImU32
/** retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList */
declare fn igGetColorU32_Vec4 as (col: ImVec4): ImU32
declare fn igGetColorU32 extern "igGetColorU32_Vec4" as (col: ImVec4): ImU32
/** retrieve given color with style alpha applied, packed as a 32-bit value suitable for ImDrawList */
declare fn igGetColorU32_U32 as (col: ImU32, alpha_mul: Float = 1.0f): ImU32
declare fn igGetColorU32 extern "igGetColorU32_U32" as (col: ImU32, alpha_mul: Float = 1.0f): ImU32
/** retrieve style color as stored in ImGuiStyle structure. use to feed back into PushStyleColor(), otherwise use GetColorU32() to get style color with style alpha baked in. */
declare fn igGetStyleColorVec4 as (idx: ImGuiCol): @ImVec4
/** cursor position, absolute coordinates. THIS IS YOUR BEST FRIEND (prefer using this rather than GetCursorPos(), also more useful to work with ImDrawList API). */
declare fn igGetCursorScreenPos as (): ImVec2
/** cursor position, absolute coordinates. THIS IS YOUR BEST FRIEND. */
declare fn igSetCursorScreenPos as (pos: ImVec2)
/** available space from current position. THIS IS YOUR BEST FRIEND. */
declare fn igGetContentRegionAvail as (): ImVec2
/** [window-local] cursor position in window-local coordinates. This is not your best friend. */
declare fn igGetCursorPos as (): ImVec2
/** [window-local] " */
declare fn igGetCursorPosX as (): Float
/** [window-local] " */
declare fn igGetCursorPosY as (): Float
/** [window-local] " */
declare fn igSetCursorPos as (local_pos: ImVec2)
/** [window-local] " */
declare fn igSetCursorPosX as (local_x: Float)
/** [window-local] " */
declare fn igSetCursorPosY as (local_y: Float)
/** [window-local] initial cursor position, in window-local coordinates. Call GetCursorScreenPos() after Begin() to get the absolute coordinates version. */
declare fn igGetCursorStartPos as (): ImVec2
/** separator, generally horizontal. inside a menu bar or in horizontal layout mode, this becomes a vertical separator. */
declare fn igSeparator as ()
/** call between widgets or groups to layout them horizontally. X position given in window coordinates. */
declare fn igSameLine as (offset_from_start_x: Float = 0.0f, spacing: Float = -1.0f)
/** undo a SameLine() or force a new line when in a horizontal-layout context. */
declare fn igNewLine as ()
/** add vertical spacing. */
declare fn igSpacing as ()
/** add a dummy item of given size. unlike InvisibleButton(), Dummy() won't take the mouse click or be navigable into. */
declare fn igDummy as (size: ImVec2)
/** move content position toward the right, by indent_w, or style.IndentSpacing if indent_w <= 0 */
declare fn igIndent as (indent_w: Float = 0.0f)
/** move content position back to the left, by indent_w, or style.IndentSpacing if indent_w <= 0 */
declare fn igUnindent as (indent_w: Float = 0.0f)
/** lock horizontal starting position */
declare fn igBeginGroup as ()
/** unlock horizontal starting position + capture the whole group bounding box into one "item" (so you can use IsItemHovered() or layout primitives such as SameLine() on whole group, etc.) */
declare fn igEndGroup as ()
/** vertically align upcoming text baseline to FramePadding.y so that it will align properly to regularly framed items (call if you have text on a line before a framed item) */
declare fn igAlignTextToFramePadding as ()
/** ~ FontSize */
declare fn igGetTextLineHeight as (): Float
/** ~ FontSize + style.ItemSpacing.y (distance in pixels between 2 consecutive lines of text) */
declare fn igGetTextLineHeightWithSpacing as (): Float
/** ~ FontSize + style.FramePadding.y * 2 */
declare fn igGetFrameHeight as (): Float
/** ~ FontSize + style.FramePadding.y * 2 + style.ItemSpacing.y (distance in pixels between 2 consecutive lines of framed widgets) */
declare fn igGetFrameHeightWithSpacing as (): Float
/** push string into the ID stack (will hash string). */
declare fn igPushID_Str as (str_id: CString)
declare fn igPushID extern "igPushID_Str" as (str_id: CString)
/** push string into the ID stack (will hash string). */
declare fn igPushID_StrStr as (str_id_begin: CString, str_id_end: CString)
declare fn igPushID extern "igPushID_StrStr" as (str_id_begin: CString, str_id_end: CString)
/** push pointer into the ID stack (will hash pointer). */
declare fn igPushID_Ptr as (ptr_id: @Any?)
declare fn igPushID extern "igPushID_Ptr" as (ptr_id: @Any?)
/** push integer into the ID stack (will hash integer). */
declare fn igPushID_Int as (int_id: Int)
declare fn igPushID extern "igPushID_Int" as (int_id: Int)
/** pop from the ID stack. */
declare fn igPopID as ()
/** calculate unique ID (hash of whole ID stack + given parameter). e.g. if you want to query into ImGuiStorage yourself */
declare fn igGetID_Str as (str_id: CString): ImGuiID
declare fn igGetID extern "igGetID_Str" as (str_id: CString): ImGuiID
declare fn igGetID_StrStr as (str_id_begin: CString, str_id_end: CString): ImGuiID
declare fn igGetID extern "igGetID_StrStr" as (str_id_begin: CString, str_id_end: CString): ImGuiID
declare fn igGetID_Ptr as (ptr_id: @Any?): ImGuiID
declare fn igGetID extern "igGetID_Ptr" as (ptr_id: @Any?): ImGuiID
declare fn igGetID_Int as (int_id: Int): ImGuiID
declare fn igGetID extern "igGetID_Int" as (int_id: Int): ImGuiID
/** raw text without formatting. Practically equivalent to 'Text("%s", text)' but doesn't require null terminated string if 'text_end' is specified. */
declare fn igTextUnformatted as (text: CString, text_end: CString? = null)
/** formatted text */
declare fn igText as (fmt: CString, ...)
/** shortcut for PushStyleColor(ImGuiCol_Text, col); Text(fmt, ...); PopStyleColor(); */
declare fn igTextColored as (col: ImVec4, fmt: CString, ...)
/** shortcut for PushStyleColor(ImGuiCol_Text, style.Colors[ImGuiCol_TextDisabled]); Text(fmt, ...); PopStyleColor(); */
declare fn igTextDisabled as (fmt: CString, ...)
/** shortcut for PushTextWrapPos(0.0f); Text(fmt, ...); PopTextWrapPos();. Note that this won't work on an auto-resizing window if there's no other widgets to extend the window width, yoy may need to set a size using SetNextWindowSize(). */
declare fn igTextWrapped as (fmt: CString, ...)
/** display text+label aligned the same way as value+label widgets */
declare fn igLabelText as (label: CString, fmt: CString, ...)
/** shortcut for Bullet()+Text() */
declare fn igBulletText as (fmt: CString, ...)
/** currently: formatted text with a horizontal line */
declare fn igSeparatorText as (label: CString)
/** button */
declare fn igButton as (label: CString, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
/** button with (FramePadding.y == 0) to easily embed within text */
declare fn igSmallButton as (label: CString): Bool
/** flexible button behavior without the visuals, frequently useful to build custom behaviors using the public api (along with IsItemActive, IsItemHovered, etc.) */
declare fn igInvisibleButton as (str_id: CString, size: ImVec2, flags: ImGuiButtonFlags = 0): Bool
/** square button with an arrow shape */
declare fn igArrowButton as (str_id: CString, dir: ImGuiDir): Bool
declare fn igCheckbox as (label: CString, mut v: Bool): Bool
declare fn igCheckboxFlags_IntPtr as (label: CString, mut flags: Int, flags_value: Int): Bool
declare fn igCheckboxFlags extern "igCheckboxFlags_IntPtr" as (label: CString, mut flags: Int, flags_value: Int): Bool
declare fn igCheckboxFlags_UintPtr as (label: CString, mut flags: UInt, flags_value: UInt): Bool
declare fn igCheckboxFlags extern "igCheckboxFlags_UintPtr" as (label: CString, mut flags: UInt, flags_value: UInt): Bool
/** use with e.g. if (RadioButton("one", my_value==1)) { my_value = 1; } */
declare fn igRadioButton_Bool as (label: CString, active: Bool): Bool
declare fn igRadioButton extern "igRadioButton_Bool" as (label: CString, active: Bool): Bool
/** shortcut to handle the above pattern when value is an integer */
declare fn igRadioButton_IntPtr as (label: CString, mut v: Int, v_button: Int): Bool
declare fn igRadioButton extern "igRadioButton_IntPtr" as (label: CString, mut v: Int, v_button: Int): Bool
declare fn igProgressBar as (fraction: Float, size_arg: ImVec2 = ImVec2 { -FLT_MIN, 0.0f }, overlay: CString? = null)
/** draw a small circle + keep the cursor on the same line. advance cursor x position by GetTreeNodeToLabelSpacing(), same distance that TreeNode() uses */
declare fn igBullet as ()
/** hyperlink text button, return true when clicked */
declare fn igTextLink as (label: CString): Bool
/** hyperlink text button, automatically open file/url when clicked */
declare fn igTextLinkOpenURL as (label: CString, url: CString? = null): Bool
declare fn igImage as (tex_ref: ImTextureRef, image_size: ImVec2, uv0: ImVec2 = ImVec2 { 0.0f, 0.0f }, uv1: ImVec2 = ImVec2 { 1.0f, 1.0f })
declare fn igImageWithBg as (tex_ref: ImTextureRef, image_size: ImVec2, uv0: ImVec2 = ImVec2 { 0.0f, 0.0f }, uv1: ImVec2 = ImVec2 { 1.0f, 1.0f }, bg_col: ImVec4 = ImVec4 { 0.0f, 0.0f, 0.0f, 0.0f }, tint_col: ImVec4 = ImVec4 { 1.0f, 1.0f, 1.0f, 1.0f })
declare fn igImageButton as (str_id: CString, tex_ref: ImTextureRef, image_size: ImVec2, uv0: ImVec2 = ImVec2 { 0.0f, 0.0f }, uv1: ImVec2 = ImVec2 { 1.0f, 1.0f }, bg_col: ImVec4 = ImVec4 { 0.0f, 0.0f, 0.0f, 0.0f }, tint_col: ImVec4 = ImVec4 { 1.0f, 1.0f, 1.0f, 1.0f }): Bool
declare fn igBeginCombo as (label: CString, preview_value: CString, flags: ImGuiComboFlags = 0): Bool
/** only call EndCombo() if BeginCombo() returns true! */
declare fn igEndCombo as ()
declare fn igCombo_Str_arr as (label: CString, mut current_item: Int, items: CString[]c, items_count: Int, popup_max_height_in_items: Int = -1): Bool
declare fn igCombo extern "igCombo_Str_arr" as (label: CString, mut current_item: Int, items: CString[]c, items_count: Int, popup_max_height_in_items: Int = -1): Bool
/** Separate items with \0 within a string, end item-list with \0\0. e.g. "One\0Two\0Three\0" */
declare fn igCombo_Str as (label: CString, mut current_item: Int, items_separated_by_zeros: CString, popup_max_height_in_items: Int = -1): Bool
declare fn igCombo extern "igCombo_Str" as (label: CString, mut current_item: Int, items_separated_by_zeros: CString, popup_max_height_in_items: Int = -1): Bool
declare fn igCombo_FnStrPtr as (label: CString, mut current_item: Int, getter: (user_data: @Any?, idx: Int) => CString?, user_data: @Any?, items_count: Int, popup_max_height_in_items: Int = -1): Bool
declare fn igCombo extern "igCombo_FnStrPtr" as (label: CString, mut current_item: Int, getter: (user_data: @Any?, idx: Int) => CString?, user_data: @Any?, items_count: Int, popup_max_height_in_items: Int = -1): Bool
/** If v_min >= v_max we have no bound */
declare fn igDragFloat as (label: CString, mut v: Float, v_speed: Float = 1.0f, v_min: Float = 0.0f, v_max: Float = 0.0f, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragFloat2 as (label: CString, v: Float[]c, v_speed: Float = 1.0f, v_min: Float = 0.0f, v_max: Float = 0.0f, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragFloat3 as (label: CString, v: Float[]c, v_speed: Float = 1.0f, v_min: Float = 0.0f, v_max: Float = 0.0f, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragFloat4 as (label: CString, v: Float[]c, v_speed: Float = 1.0f, v_min: Float = 0.0f, v_max: Float = 0.0f, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragFloatRange2 as (label: CString, mut v_current_min: Float, mut v_current_max: Float, v_speed: Float = 1.0f, v_min: Float = 0.0f, v_max: Float = 0.0f, format: CString = "%.3f"c, format_max: CString? = null, flags: ImGuiSliderFlags = 0): Bool
/** If v_min >= v_max we have no bound */
declare fn igDragInt as (label: CString, mut v: Int, v_speed: Float = 1.0f, v_min: Int = 0, v_max: Int = 0, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragInt2 as (label: CString, v: Int[]c, v_speed: Float = 1.0f, v_min: Int = 0, v_max: Int = 0, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragInt3 as (label: CString, v: Int[]c, v_speed: Float = 1.0f, v_min: Int = 0, v_max: Int = 0, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragInt4 as (label: CString, v: Int[]c, v_speed: Float = 1.0f, v_min: Int = 0, v_max: Int = 0, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragIntRange2 as (label: CString, mut v_current_min: Int, mut v_current_max: Int, v_speed: Float = 1.0f, v_min: Int = 0, v_max: Int = 0, format: CString = "%d"c, format_max: CString? = null, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragScalar as (label: CString, data_type: ImGuiDataType, p_data: @Any?, v_speed: Float = 1.0f, p_min: @Any? = null, p_max: @Any? = null, format: CString? = null, flags: ImGuiSliderFlags = 0): Bool
declare fn igDragScalarN as (label: CString, data_type: ImGuiDataType, p_data: @Any?, components: Int, v_speed: Float = 1.0f, p_min: @Any? = null, p_max: @Any? = null, format: CString? = null, flags: ImGuiSliderFlags = 0): Bool
/** adjust format to decorate the value with a prefix or a suffix for in-slider labels or unit display. */
declare fn igSliderFloat as (label: CString, mut v: Float, v_min: Float, v_max: Float, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderFloat2 as (label: CString, v: Float[]c, v_min: Float, v_max: Float, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderFloat3 as (label: CString, v: Float[]c, v_min: Float, v_max: Float, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderFloat4 as (label: CString, v: Float[]c, v_min: Float, v_max: Float, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderAngle as (label: CString, mut v_rad: Float, v_degrees_min: Float = -360.0f, v_degrees_max: Float = 360.0f, format: CString = "%.0f deg"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderInt as (label: CString, mut v: Int, v_min: Int, v_max: Int, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderInt2 as (label: CString, v: Int[]c, v_min: Int, v_max: Int, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderInt3 as (label: CString, v: Int[]c, v_min: Int, v_max: Int, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderInt4 as (label: CString, v: Int[]c, v_min: Int, v_max: Int, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderScalar as (label: CString, data_type: ImGuiDataType, p_data: @Any?, p_min: @Any?, p_max: @Any?, format: CString? = null, flags: ImGuiSliderFlags = 0): Bool
declare fn igSliderScalarN as (label: CString, data_type: ImGuiDataType, p_data: @Any?, components: Int, p_min: @Any?, p_max: @Any?, format: CString? = null, flags: ImGuiSliderFlags = 0): Bool
declare fn igVSliderFloat as (label: CString, size: ImVec2, mut v: Float, v_min: Float, v_max: Float, format: CString = "%.3f"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igVSliderInt as (label: CString, size: ImVec2, mut v: Int, v_min: Int, v_max: Int, format: CString = "%d"c, flags: ImGuiSliderFlags = 0): Bool
declare fn igVSliderScalar as (label: CString, size: ImVec2, data_type: ImGuiDataType, p_data: @Any?, p_min: @Any?, p_max: @Any?, format: CString? = null, flags: ImGuiSliderFlags = 0): Bool
declare fn igInputText as (label: CString, buf: Char[]c, buf_size: Size, flags: ImGuiInputTextFlags = 0, callback: ImGuiInputTextCallback? = null, user_data: @Any? = null): Bool
declare fn igInputTextMultiline as (label: CString, buf: Char[]c, buf_size: Size, size: ImVec2 = ImVec2 { 0.0f, 0.0f }, flags: ImGuiInputTextFlags = 0, callback: ImGuiInputTextCallback? = null, user_data: @Any? = null): Bool
declare fn igInputTextWithHint as (label: CString, hint: CString, buf: Char[]c, buf_size: Size, flags: ImGuiInputTextFlags = 0, callback: ImGuiInputTextCallback? = null, user_data: @Any? = null): Bool
declare fn igInputFloat as (label: CString, mut v: Float, step: Float = 0.0f, step_fast: Float = 0.0f, format: CString = "%.3f"c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputFloat2 as (label: CString, v: Float[]c, format: CString = "%.3f"c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputFloat3 as (label: CString, v: Float[]c, format: CString = "%.3f"c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputFloat4 as (label: CString, v: Float[]c, format: CString = "%.3f"c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputInt as (label: CString, mut v: Int, step: Int = 1, step_fast: Int = 100, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputInt2 as (label: CString, v: Int[]c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputInt3 as (label: CString, v: Int[]c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputInt4 as (label: CString, v: Int[]c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputDouble as (label: CString, mut v: Double, step: Double = 0.0, step_fast: Double = 0.0, format: CString = "%.6f"c, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputScalar as (label: CString, data_type: ImGuiDataType, p_data: @Any?, p_step: @Any? = null, p_step_fast: @Any? = null, format: CString? = null, flags: ImGuiInputTextFlags = 0): Bool
declare fn igInputScalarN as (label: CString, data_type: ImGuiDataType, p_data: @Any?, components: Int, p_step: @Any? = null, p_step_fast: @Any? = null, format: CString? = null, flags: ImGuiInputTextFlags = 0): Bool
declare fn igColorEdit3 as (label: CString, col: Float[]c, flags: ImGuiColorEditFlags = 0): Bool
declare fn igColorEdit4 as (label: CString, col: Float[]c, flags: ImGuiColorEditFlags = 0): Bool
declare fn igColorPicker3 as (label: CString, col: Float[]c, flags: ImGuiColorEditFlags = 0): Bool
declare fn igColorPicker4 as (label: CString, col: Float[]c, flags: ImGuiColorEditFlags = 0, ref_col: Float[]c? = null): Bool
/** display a color square/button, hover for details, return true when pressed. */
declare fn igColorButton as (desc_id: CString, col: ImVec4, flags: ImGuiColorEditFlags = 0, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
declare fn igTreeNode_Str as (label: CString): Bool
declare fn igTreeNode extern "igTreeNode_Str" as (label: CString): Bool
/** helper variation to easily decorrelate the id from the displayed string. Read the FAQ about why and how to use ID. to align arbitrary text at the same level as a TreeNode() you can use Bullet(). */
declare fn igTreeNode_StrStr as (str_id: CString, fmt: CString, ...): Bool
declare fn igTreeNode extern "igTreeNode_StrStr" as (str_id: CString, fmt: CString, ...): Bool
/** " */
declare fn igTreeNode_Ptr as (ptr_id: @Any?, fmt: CString, ...): Bool
declare fn igTreeNode extern "igTreeNode_Ptr" as (ptr_id: @Any?, fmt: CString, ...): Bool
declare fn igTreeNodeEx_Str as (label: CString, flags: ImGuiTreeNodeFlags = 0): Bool
declare fn igTreeNodeEx extern "igTreeNodeEx_Str" as (label: CString, flags: ImGuiTreeNodeFlags = 0): Bool
declare fn igTreeNodeEx_StrStr as (str_id: CString, flags: ImGuiTreeNodeFlags, fmt: CString, ...): Bool
declare fn igTreeNodeEx extern "igTreeNodeEx_StrStr" as (str_id: CString, flags: ImGuiTreeNodeFlags, fmt: CString, ...): Bool
declare fn igTreeNodeEx_Ptr as (ptr_id: @Any?, flags: ImGuiTreeNodeFlags, fmt: CString, ...): Bool
declare fn igTreeNodeEx extern "igTreeNodeEx_Ptr" as (ptr_id: @Any?, flags: ImGuiTreeNodeFlags, fmt: CString, ...): Bool
/** ~ Indent()+PushID(). Already called by TreeNode() when returning true, but you can call TreePush/TreePop yourself if desired. */
declare fn igTreePush_Str as (str_id: CString)
declare fn igTreePush extern "igTreePush_Str" as (str_id: CString)
/** " */
declare fn igTreePush_Ptr as (ptr_id: @Any?)
declare fn igTreePush extern "igTreePush_Ptr" as (ptr_id: @Any?)
/** ~ Unindent()+PopID() */
declare fn igTreePop as ()
/** horizontal distance preceding label when using TreeNode*() or Bullet() == (g.FontSize + style.FramePadding.x*2) for a regular unframed TreeNode */
declare fn igGetTreeNodeToLabelSpacing as (): Float
/** if returning 'true' the header is open. doesn't indent nor push on ID stack. user doesn't have to call TreePop(). */
declare fn igCollapsingHeader_TreeNodeFlags as (label: CString, flags: ImGuiTreeNodeFlags = 0): Bool
declare fn igCollapsingHeader extern "igCollapsingHeader_TreeNodeFlags" as (label: CString, flags: ImGuiTreeNodeFlags = 0): Bool
/** when 'p_visible != NULL': if '*p_visible==true' display an additional small close button on upper right of the header which will set the bool to false when clicked, if '*p_visible==false' don't display the header. */
declare fn igCollapsingHeader_BoolPtr as (label: CString, mut p_visible: Bool, flags: ImGuiTreeNodeFlags = 0): Bool
declare fn igCollapsingHeader extern "igCollapsingHeader_BoolPtr" as (label: CString, mut p_visible: Bool, flags: ImGuiTreeNodeFlags = 0): Bool
/** set next TreeNode/CollapsingHeader open state. */
declare fn igSetNextItemOpen as (is_open: Bool, cond: ImGuiCond = 0)
/** set id to use for open/close storage (default to same as item id). */
declare fn igSetNextItemStorageID as (storage_id: ImGuiID)
/** retrieve tree node open/close state. */
declare fn igTreeNodeGetOpen as (storage_id: ImGuiID): Bool
/** "bool selected" carry the selection state (read-only). Selectable() is clicked is returns true so you can modify your selection state. size.x==0.0: use remaining width, size.x>0.0: specify width. size.y==0.0: use label height, size.y>0.0: specify height */
declare fn igSelectable_Bool as (label: CString, selected: Bool = false, flags: ImGuiSelectableFlags = 0, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
declare fn igSelectable extern "igSelectable_Bool" as (label: CString, selected: Bool = false, flags: ImGuiSelectableFlags = 0, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
/** "bool* p_selected" point to the selection state (read-write), as a convenient helper. */
declare fn igSelectable_BoolPtr as (label: CString, mut p_selected: Bool, flags: ImGuiSelectableFlags = 0, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
declare fn igSelectable extern "igSelectable_BoolPtr" as (label: CString, mut p_selected: Bool, flags: ImGuiSelectableFlags = 0, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
declare fn igBeginMultiSelect as (flags: ImGuiMultiSelectFlags, selection_size: Int = -1, items_count: Int = -1): @ImGuiMultiSelectIO?
declare fn igEndMultiSelect as (): @ImGuiMultiSelectIO?
declare fn igSetNextItemSelectionUserData as (selection_user_data: ImGuiSelectionUserData)
/** Was the last item selection state toggled? Useful if you need the per-item information _before_ reaching EndMultiSelect(). We only returns toggle _event_ in order to handle clipping correctly. */
declare fn igIsItemToggledSelection as (): Bool
/** open a framed scrolling region */
declare fn igBeginListBox as (label: CString, size: ImVec2 = ImVec2 { 0.0f, 0.0f }): Bool
/** only call EndListBox() if BeginListBox() returned true! */
declare fn igEndListBox as ()
declare fn igListBox_Str_arr as (label: CString, mut current_item: Int, items: CString[]c, items_count: Int, height_in_items: Int = -1): Bool
declare fn igListBox extern "igListBox_Str_arr" as (label: CString, mut current_item: Int, items: CString[]c, items_count: Int, height_in_items: Int = -1): Bool
declare fn igListBox_FnStrPtr as (label: CString, mut current_item: Int, getter: (user_data: @Any?, idx: Int) => CString?, user_data: @Any?, items_count: Int, height_in_items: Int = -1): Bool
declare fn igListBox extern "igListBox_FnStrPtr" as (label: CString, mut current_item: Int, getter: (user_data: @Any?, idx: Int) => CString?, user_data: @Any?, items_count: Int, height_in_items: Int = -1): Bool
declare fn igPlotLines_FloatPtr as (label: CString, values: Float[]c, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f }, stride: Int = sizeof(Float) as Int)
declare fn igPlotLines extern "igPlotLines_FloatPtr" as (label: CString, values: Float[]c, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f }, stride: Int = sizeof(Float) as Int)
declare fn igPlotLines_FnFloatPtr as (label: CString, values_getter: (data: @Any?, idx: Int) => Float, data: @Any?, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f })
declare fn igPlotLines extern "igPlotLines_FnFloatPtr" as (label: CString, values_getter: (data: @Any?, idx: Int) => Float, data: @Any?, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f })
declare fn igPlotHistogram_FloatPtr as (label: CString, values: Float[]c, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f }, stride: Int = sizeof(Float) as Int)
declare fn igPlotHistogram extern "igPlotHistogram_FloatPtr" as (label: CString, values: Float[]c, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f }, stride: Int = sizeof(Float) as Int)
declare fn igPlotHistogram_FnFloatPtr as (label: CString, values_getter: (data: @Any?, idx: Int) => Float, data: @Any?, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f })
declare fn igPlotHistogram extern "igPlotHistogram_FnFloatPtr" as (label: CString, values_getter: (data: @Any?, idx: Int) => Float, data: @Any?, values_count: Int, values_offset: Int = 0, overlay_text: CString? = null, scale_min: Float = FLT_MAX, scale_max: Float = FLT_MAX, graph_size: ImVec2 = ImVec2 { 0.0f, 0.0f })
declare fn igValue_Bool as (prefix: CString, b: Bool)
declare fn igValue extern "igValue_Bool" as (prefix: CString, b: Bool)
declare fn igValue_Int as (prefix: CString, v: Int)
declare fn igValue extern "igValue_Int" as (prefix: CString, v: Int)
declare fn igValue_Uint as (prefix: CString, v: UInt)
declare fn igValue extern "igValue_Uint" as (prefix: CString, v: UInt)
declare fn igValue_Float as (prefix: CString, v: Float, float_format: CString? = null)
declare fn igValue extern "igValue_Float" as (prefix: CString, v: Float, float_format: CString? = null)
/** append to menu-bar of current window (requires ImGuiWindowFlags_MenuBar flag set on parent window). */
declare fn igBeginMenuBar as (): Bool
/** only call EndMenuBar() if BeginMenuBar() returns true! */
declare fn igEndMenuBar as ()
/** create and append to a full screen menu-bar. */
declare fn igBeginMainMenuBar as (): Bool
/** only call EndMainMenuBar() if BeginMainMenuBar() returns true! */
declare fn igEndMainMenuBar as ()
/** create a sub-menu entry. only call EndMenu() if this returns true! */
declare fn igBeginMenu as (label: CString, enabled: Bool = true): Bool
/** only call EndMenu() if BeginMenu() returns true! */
declare fn igEndMenu as ()
/** return true when activated. */
declare fn igMenuItem_Bool as (label: CString, shortcut: CString? = null, selected: Bool = false, enabled: Bool = true): Bool
declare fn igMenuItem extern "igMenuItem_Bool" as (label: CString, shortcut: CString? = null, selected: Bool = false, enabled: Bool = true): Bool
/** return true when activated + toggle (*p_selected) if p_selected != NULL */
declare fn igMenuItem_BoolPtr as (label: CString, shortcut: CString, mut p_selected: Bool, enabled: Bool = true): Bool
declare fn igMenuItem extern "igMenuItem_BoolPtr" as (label: CString, shortcut: CString, mut p_selected: Bool, enabled: Bool = true): Bool
/** begin/append a tooltip window. */
declare fn igBeginTooltip as (): Bool
/** only call EndTooltip() if BeginTooltip()/BeginItemTooltip() returns true! */
declare fn igEndTooltip as ()
/** set a text-only tooltip. Often used after a ImGui::IsItemHovered() check. Override any previous call to SetTooltip(). */
declare fn igSetTooltip as (fmt: CString, ...)
/** begin/append a tooltip window if preceding item was hovered. */
declare fn igBeginItemTooltip as (): Bool
/** set a text-only tooltip if preceding item was hovered. override any previous call to SetTooltip(). */
declare fn igSetItemTooltip as (fmt: CString, ...)
/** return true if the popup is open, and you can start outputting to it. */
declare fn igBeginPopup as (str_id: CString, flags: ImGuiWindowFlags = 0): Bool
/** return true if the modal is open, and you can start outputting to it. */
declare fn igBeginPopupModal as (name: CString, p_open: @Bool? = null, flags: ImGuiWindowFlags = 0): Bool
/** only call EndPopup() if BeginPopupXXX() returns true! */
declare fn igEndPopup as ()
/** call to mark popup as open (don't call every frame!). */
declare fn igOpenPopup_Str as (str_id: CString, popup_flags: ImGuiPopupFlags = 0): Bool
declare fn igOpenPopup extern "igOpenPopup_Str" as (str_id: CString, popup_flags: ImGuiPopupFlags = 0): Bool
/** id overload to facilitate calling from nested stacks */
declare fn igOpenPopup_ID as (id: ImGuiID, popup_flags: ImGuiPopupFlags = 0): Bool
declare fn igOpenPopup extern "igOpenPopup_ID" as (id: ImGuiID, popup_flags: ImGuiPopupFlags = 0): Bool
/** helper to open popup when clicked on last item. Default to ImGuiPopupFlags_MouseButtonRight == 1. (note: actually triggers on the mouse _released_ event to be consistent with popup behaviors) */
declare fn igOpenPopupOnItemClick as (str_id: CString? = null, popup_flags: ImGuiPopupFlags = 0): Bool
/** manually close the popup we have begin-ed into. */
declare fn igCloseCurrentPopup as ()
/** open+begin popup when clicked on last item. Use str_id==NULL to associate the popup to previous item. If you want to use that on a non-interactive item such as Text() you need to pass in an explicit ID here. read comments in .cpp! */
declare fn igBeginPopupContextItem as (str_id: CString? = null, popup_flags: ImGuiPopupFlags = 0): Bool
/** open+begin popup when clicked on current window. */
declare fn igBeginPopupContextWindow as (str_id: CString? = null, popup_flags: ImGuiPopupFlags = 0): Bool
/** open+begin popup when clicked in void (where there are no windows). */
declare fn igBeginPopupContextVoid as (str_id: CString? = null, popup_flags: ImGuiPopupFlags = 0): Bool
/** return true if the popup is open. */
declare fn igIsPopupOpen_Str as (str_id: CString, flags: ImGuiPopupFlags = 0): Bool
declare fn igIsPopupOpen extern "igIsPopupOpen_Str" as (str_id: CString, flags: ImGuiPopupFlags = 0): Bool
declare fn igBeginTable as (str_id: CString, columns: Int, flags: ImGuiTableFlags = 0, outer_size: ImVec2 = ImVec2 { 0.0f, 0.0f }, inner_width: Float = 0.0f): Bool
/** only call EndTable() if BeginTable() returns true! */
declare fn igEndTable as ()
/** append into the first cell of a new row. 'min_row_height' include the minimum top and bottom padding aka CellPadding.y * 2.0f. */
declare fn igTableNextRow as (row_flags: ImGuiTableRowFlags = 0, min_row_height: Float = 0.0f)
/** append into the next column (or first column of next row if currently in last column). Return true when column is visible. */
declare fn igTableNextColumn as (): Bool
/** append into the specified column. Return true when column is visible. */
declare fn igTableSetColumnIndex as (column_n: Int): Bool
declare fn igTableSetupColumn as (label: CString, flags: ImGuiTableColumnFlags = 0, init_width_or_weight: Float = 0.0f, user_data: ImGuiID = 0)
/** lock columns/rows so they stay visible when scrolled. */
declare fn igTableSetupScrollFreeze as (cols: Int, rows: Int)
/** submit one header cell manually (rarely used) */
declare fn igTableHeader as (label: CString)
/** submit a row with headers cells based on data provided to TableSetupColumn() + submit context menu */
declare fn igTableHeadersRow as ()
/** submit a row with angled headers for every column with the ImGuiTableColumnFlags_AngledHeader flag. MUST BE FIRST ROW. */
declare fn igTableAngledHeadersRow as ()
/** get latest sort specs for the table (NULL if not sorting).  Lifetime: don't hold on this pointer over multiple frames or past any subsequent call to BeginTable(). */
declare fn igTableGetSortSpecs as (): @ImGuiTableSortSpecs?
/** return number of columns (value passed to BeginTable) */
declare fn igTableGetColumnCount as (): Int
/** return current column index. */
declare fn igTableGetColumnIndex as (): Int
/** return current row index (header rows are accounted for) */
declare fn igTableGetRowIndex as (): Int
/** return "" if column didn't have a name declared by TableSetupColumn(). Pass -1 to use current column. */
declare fn igTableGetColumnName_Int as (column_n: Int = -1): CString?
declare fn igTableGetColumnName extern "igTableGetColumnName_Int" as (column_n: Int = -1): CString?
/** return column flags so you can query their Enabled/Visible/Sorted/Hovered status flags. Pass -1 to use current column. */
declare fn igTableGetColumnFlags as (column_n: Int = -1): ImGuiTableColumnFlags
/** change user accessible enabled/disabled state of a column. Set to false to hide the column. User can use the context menu to change this themselves (right-click in headers, or right-click in columns body with ImGuiTableFlags_ContextMenuInBody) */
declare fn igTableSetColumnEnabled as (column_n: Int, v: Bool)
/** return hovered column. return -1 when table is not hovered. return columns_count if the unused space at the right of visible columns is hovered. Can also use (TableGetColumnFlags() & ImGuiTableColumnFlags_IsHovered) instead. */
declare fn igTableGetHoveredColumn as (): Int
/** change the color of a cell, row, or column. See ImGuiTableBgTarget_ flags for details. */
declare fn igTableSetBgColor as (target: ImGuiTableBgTarget, color: ImU32, column_n: Int = -1)
declare fn igColumns as (count: Int = 1, id: CString? = null, borders: Bool = true)
/** next column, defaults to current row or next row if the current row is finished */
declare fn igNextColumn as ()
/** get current column index */
declare fn igGetColumnIndex as (): Int
/** get column width (in pixels). pass -1 to use current column */
declare fn igGetColumnWidth as (column_index: Int = -1): Float
/** set column width (in pixels). pass -1 to use current column */
declare fn igSetColumnWidth as (column_index: Int, width: Float)
/** get position of column line (in pixels, from the left side of the contents region). pass -1 to use current column, otherwise 0..GetColumnsCount() inclusive. column 0 is typically 0.0f */
declare fn igGetColumnOffset as (column_index: Int = -1): Float
/** set position of column line (in pixels, from the left side of the contents region). pass -1 to use current column */
declare fn igSetColumnOffset as (column_index: Int, offset_x: Float)
declare fn igGetColumnsCount as (): Int
/** create and append into a TabBar */
declare fn igBeginTabBar as (str_id: CString, flags: ImGuiTabBarFlags = 0): Bool
/** only call EndTabBar() if BeginTabBar() returns true! */
declare fn igEndTabBar as ()
/** create a Tab. Returns true if the Tab is selected. */
declare fn igBeginTabItem as (label: CString, p_open: @Bool? = null, flags: ImGuiTabItemFlags = 0): Bool
/** only call EndTabItem() if BeginTabItem() returns true! */
declare fn igEndTabItem as ()
/** create a Tab behaving like a button. return true when clicked. cannot be selected in the tab bar. */
declare fn igTabItemButton as (label: CString, flags: ImGuiTabItemFlags = 0): Bool
/** notify TabBar or Docking system of a closed tab/window ahead (useful to reduce visual flicker on reorderable tab bars). For tab-bar: call after BeginTabBar() and before Tab submissions. Otherwise call with a window name. */
declare fn igSetTabItemClosed as (tab_or_docked_window_label: CString)
/** start logging to tty (stdout) */
declare fn igLogToTTY as (auto_open_depth: Int = -1)
/** start logging to file */
declare fn igLogToFile as (auto_open_depth: Int = -1, filename: CString? = null)
/** start logging to OS clipboard */
declare fn igLogToClipboard as (auto_open_depth: Int = -1)
/** stop logging (close file, etc.) */
declare fn igLogFinish as ()
/** helper to display buttons for logging to tty/file/clipboard */
declare fn igLogButtons as ()
/** pass text data straight to log (without being displayed) */
declare fn igLogText as (fmt: CString, ...)
/** call after submitting an item which may be dragged. when this return true, you can call SetDragDropPayload() + EndDragDropSource() */
declare fn igBeginDragDropSource as (flags: ImGuiDragDropFlags = 0): Bool
/** type is a user defined string of maximum 32 characters. Strings starting with '_' are reserved for dear imgui internal types. Data is copied and held by imgui. Return true when payload has been accepted. */
declare fn igSetDragDropPayload as (type_: CString, data: @Any?, sz: Size, cond: ImGuiCond = 0): Bool
/** only call EndDragDropSource() if BeginDragDropSource() returns true! */
declare fn igEndDragDropSource as ()
/** call after submitting an item that may receive a payload. If this returns true, you can call AcceptDragDropPayload() + EndDragDropTarget() */
declare fn igBeginDragDropTarget as (): Bool
/** accept contents of a given type. If ImGuiDragDropFlags_AcceptBeforeDelivery is set you can peek into the payload before the mouse button is released. */
declare fn igAcceptDragDropPayload as (type_: CString, flags: ImGuiDragDropFlags = 0): @ImGuiPayload?
/** only call EndDragDropTarget() if BeginDragDropTarget() returns true! */
declare fn igEndDragDropTarget as ()
/** peek directly into the current payload from anywhere. returns NULL when drag and drop is finished or inactive. use ImGuiPayload::IsDataType() to test for the payload type. */
declare fn igGetDragDropPayload as (): @ImGuiPayload?
declare fn igBeginDisabled as (disabled: Bool = true)
declare fn igEndDisabled as ()
declare fn igPushClipRect as (clip_rect_min: ImVec2, clip_rect_max: ImVec2, intersect_with_current_clip_rect: Bool)
declare fn igPopClipRect as ()
/** make last item the default focused item of a newly appearing window. */
declare fn igSetItemDefaultFocus as ()
/** focus keyboard on the next widget. Use positive 'offset' to access sub components of a multiple component widget. Use -1 to access previous widget. */
declare fn igSetKeyboardFocusHere as (offset: Int = 0)
/** alter visibility of keyboard/gamepad cursor. by default: show when using an arrow key, hide when clicking with mouse. */
declare fn igSetNavCursorVisible as (visible: Bool)
/** allow next item to be overlapped by a subsequent item. Typically useful with InvisibleButton(), Selectable(), TreeNode() covering an area where subsequent items may need to be added. Note that both Selectable() and TreeNode() have dedicated flags doing this. */
declare fn igSetNextItemAllowOverlap as ()
/** is the last item hovered? (and usable, aka not blocked by a popup, etc.). See ImGuiHoveredFlags for more options. */
declare fn igIsItemHovered as (flags: ImGuiHoveredFlags = 0): Bool
/** is the last item active? (e.g. button being held, text field being edited. This will continuously return true while holding mouse button on an item. Items that don't interact will always return false) */
declare fn igIsItemActive as (): Bool
/** is the last item focused for keyboard/gamepad navigation? */
declare fn igIsItemFocused as (): Bool
/** is the last item hovered and mouse clicked on? (**)  == IsMouseClicked(mouse_button) && IsItemHovered()Important. (**) this is NOT equivalent to the behavior of e.g. Button(). Read comments in function definition. */
declare fn igIsItemClicked as (mouse_button: ImGuiMouseButton = 0): Bool
/** is the last item visible? (items may be out of sight because of clipping/scrolling) */
declare fn igIsItemVisible as (): Bool
/** did the last item modify its underlying value this frame? or was pressed? This is generally the same as the "bool" return value of many widgets. */
declare fn igIsItemEdited as (): Bool
/** was the last item just made active (item was previously inactive). */
declare fn igIsItemActivated as (): Bool
/** was the last item just made inactive (item was previously active). Useful for Undo/Redo patterns with widgets that require continuous editing. */
declare fn igIsItemDeactivated as (): Bool
/** was the last item just made inactive and made a value change when it was active? (e.g. Slider/Drag moved). Useful for Undo/Redo patterns with widgets that require continuous editing. Note that you may get false positives (some widgets such as Combo()/ListBox()/Selectable() will return true even when clicking an already selected item). */
declare fn igIsItemDeactivatedAfterEdit as (): Bool
/** was the last item open state toggled? set by TreeNode(). */
declare fn igIsItemToggledOpen as (): Bool
/** is any item hovered? */
declare fn igIsAnyItemHovered as (): Bool
/** is any item active? */
declare fn igIsAnyItemActive as (): Bool
/** is any item focused? */
declare fn igIsAnyItemFocused as (): Bool
/** get ID of last item (~~ often same ImGui::GetID(label) beforehand) */
declare fn igGetItemID as (): ImGuiID
/** get upper-left bounding rectangle of the last item (screen space) */
declare fn igGetItemRectMin as (): ImVec2
/** get lower-right bounding rectangle of the last item (screen space) */
declare fn igGetItemRectMax as (): ImVec2
/** get size of last item */
declare fn igGetItemRectSize as (): ImVec2
/** get generic flags of last item */
declare fn igGetItemFlags as (): ImGuiItemFlags
/** [BETA] building block for disambiguation between single-click and double-click. Returns 1 on single-click but delayed by io.MouseSingleClickDelay after mouse release. Returns 2+ on double-click or repeated clicks. */
declare fn igGetItemClickedCountWithSingleClickDelay as (mouse_button: ImGuiMouseButton = 0, delay: Float = -1.0f): Int
/** return primary/default viewport. This can never be NULL. */
declare fn igGetMainViewport as (): @ImGuiViewport
/** this draw list will be the first rendered one. Useful to quickly draw shapes/text behind dear imgui contents. */
declare fn igGetBackgroundDrawList_Nil as (): @ImDrawList
declare fn igGetBackgroundDrawList extern "igGetBackgroundDrawList_Nil" as (): @ImDrawList
/** this draw list will be the last rendered one. Useful to quickly draw shapes/text over dear imgui contents. */
declare fn igGetForegroundDrawList_Nil as (): @ImDrawList
declare fn igGetForegroundDrawList extern "igGetForegroundDrawList_Nil" as (): @ImDrawList
/** test if rectangle (of given size, starting from cursor position) is visible / not clipped. */
declare fn igIsRectVisible_Nil as (size: ImVec2): Bool
declare fn igIsRectVisible extern "igIsRectVisible_Nil" as (size: ImVec2): Bool
/** test if rectangle (in screen space) is visible / not clipped. to perform coarse clipping on user's side. */
declare fn igIsRectVisible_Vec2 as (rect_min: ImVec2, rect_max: ImVec2): Bool
declare fn igIsRectVisible extern "igIsRectVisible_Vec2" as (rect_min: ImVec2, rect_max: ImVec2): Bool
/** get global imgui time. incremented by io.DeltaTime every frame. */
declare fn igGetTime as (): Double
/** get global imgui frame count. incremented by 1 every frame. */
declare fn igGetFrameCount as (): Int
/** you may use this when creating your own ImDrawList instances. */
declare fn igGetDrawListSharedData as (): @ImDrawListSharedData
/** get a string corresponding to the enum value (for display, saving, etc.). */
declare fn igGetStyleColorName as (idx: ImGuiCol): CString
/** replace current window storage with our own (if you want to manipulate it yourself, typically clear subsection of it) */
declare fn igSetStateStorage as (storage: @ImGuiStorage)
declare fn igGetStateStorage as (): @ImGuiStorage
declare fn igCalcTextSize as (text: CString, text_end: CString? = null, hide_text_after_double_hash: Bool = false, wrap_width: Float = -1.0f): ImVec2
declare fn igColorConvertU32ToFloat4 as (in_: ImU32): ImVec4
declare fn igColorConvertFloat4ToU32 as (in_: ImVec4): ImU32
declare fn igColorConvertRGBtoHSV as (r: Float, g: Float, b: Float, out out_h: Float, out out_s: Float, out out_v: Float)
declare fn igColorConvertHSVtoRGB as (h: Float, s: Float, v: Float, out out_r: Float, out out_g: Float, out out_b: Float)
/** is key being held. */
declare fn igIsKeyDown_Nil as (key: ImGuiKey): Bool
declare fn igIsKeyDown extern "igIsKeyDown_Nil" as (key: ImGuiKey): Bool
/** was key pressed (went from !Down to Down)? Repeat rate uses io.KeyRepeatDelay / KeyRepeatRate. */
declare fn igIsKeyPressed_Bool as (key: ImGuiKey, repeat: Bool = true): Bool
declare fn igIsKeyPressed extern "igIsKeyPressed_Bool" as (key: ImGuiKey, repeat: Bool = true): Bool
/** was key released (went from Down to !Down)? */
declare fn igIsKeyReleased_Nil as (key: ImGuiKey): Bool
declare fn igIsKeyReleased extern "igIsKeyReleased_Nil" as (key: ImGuiKey): Bool
/** was key chord (mods + key) pressed, e.g. you can pass 'ImGuiMod_Ctrl | ImGuiKey_S' as a key-chord. This doesn't do any routing or focus check, please consider using Shortcut() function instead. */
declare fn igIsKeyChordPressed_Nil as (key_chord: ImGuiKeyChord): Bool
declare fn igIsKeyChordPressed extern "igIsKeyChordPressed_Nil" as (key_chord: ImGuiKeyChord): Bool
/** uses provided repeat rate/delay. return a count, most often 0 or 1 but might be >1 if RepeatRate is small enough that DeltaTime > RepeatRate */
declare fn igGetKeyPressedAmount as (key: ImGuiKey, repeat_delay: Float, rate: Float): Int
/** [DEBUG] returns English name of the key. Those names are provided for debugging purpose and are not meant to be saved persistently nor compared. */
declare fn igGetKeyName as (key: ImGuiKey): CString
/** Override io.WantCaptureKeyboard flag next frame (said flag is left for your application to handle, typically when true it instructs your app to ignore inputs). e.g. force capture keyboard when your widget is being hovered. This is equivalent to setting "io.WantCaptureKeyboard = want_capture_keyboard"; after the next NewFrame() call. */
declare fn igSetNextFrameWantCaptureKeyboard as (want_capture_keyboard: Bool)
declare fn igShortcut_Nil as (key_chord: ImGuiKeyChord, flags: ImGuiInputFlags = 0): Bool
declare fn igShortcut extern "igShortcut_Nil" as (key_chord: ImGuiKeyChord, flags: ImGuiInputFlags = 0): Bool
declare fn igSetNextItemShortcut as (key_chord: ImGuiKeyChord, flags: ImGuiInputFlags = 0)
/** Set key owner to last item ID if it is hovered or active. Return true when ownership has been set. Roughly equivalent to 'if (TestKeyOwner(key, GetItemID()) && (IsItemHovered() || IsItemActive())) { SetKeyOwner(key, GetItemID());'. */
declare fn igSetItemKeyOwner_Nil as (key: ImGuiKey): Bool
declare fn igSetItemKeyOwner extern "igSetItemKeyOwner_Nil" as (key: ImGuiKey): Bool
/** is mouse button held? */
declare fn igIsMouseDown_Nil as (button: ImGuiMouseButton): Bool
declare fn igIsMouseDown extern "igIsMouseDown_Nil" as (button: ImGuiMouseButton): Bool
/** did mouse button clicked? (went from !Down to Down). Same as GetMouseClickedCount() == 1. */
declare fn igIsMouseClicked_Bool as (button: ImGuiMouseButton, repeat: Bool = false): Bool
declare fn igIsMouseClicked extern "igIsMouseClicked_Bool" as (button: ImGuiMouseButton, repeat: Bool = false): Bool
/** did mouse button released? (went from Down to !Down) */
declare fn igIsMouseReleased_Nil as (button: ImGuiMouseButton): Bool
declare fn igIsMouseReleased extern "igIsMouseReleased_Nil" as (button: ImGuiMouseButton): Bool
/** did mouse button double-clicked? Same as GetMouseClickedCount() == 2. (note that a double-click will also report IsMouseClicked() == true) */
declare fn igIsMouseDoubleClicked_Nil as (button: ImGuiMouseButton): Bool
declare fn igIsMouseDoubleClicked extern "igIsMouseDoubleClicked_Nil" as (button: ImGuiMouseButton): Bool
/** delayed mouse release. Use sparingly. Prefer higher-level helper GetItemClickedCountWithSingleClickDelay(). Generally used with 'delay >= io.MouseDoubleClickTime' + combined with a 'io.MouseClickedLastCount==1' test. */
declare fn igIsMouseReleasedWithDelay as (button: ImGuiMouseButton, delay: Float = -1.0f): Bool
/** return the number of successive mouse-clicks at the time where a click happen (otherwise 0). */
declare fn igGetMouseClickedCount as (button: ImGuiMouseButton): Int
/** is mouse hovering given bounding rect (in screen space). clipped by current clipping settings, but disregarding of other consideration of focus/window ordering/popup-block. */
declare fn igIsMouseHoveringRect as (r_min: ImVec2, r_max: ImVec2, clip: Bool = true): Bool
/** by convention we use (-FLT_MAX,-FLT_MAX) to denote that there is no mouse available */
declare fn igIsMousePosValid as (mouse_pos: @ImVec2? = null): Bool
/** [WILL OBSOLETE] is any mouse button held? This was designed for backends, but prefer having backend maintain a mask of held mouse buttons, because upcoming input queue system will make this invalid. */
declare fn igIsAnyMouseDown as (): Bool
/** shortcut to ImGui::GetIO().MousePos provided by user, to be consistent with other calls */
declare fn igGetMousePos as (): ImVec2
/** retrieve mouse position at the time of opening popup we have BeginPopup() into (helper to avoid user backing that value themselves) */
declare fn igGetMousePosOnOpeningCurrentPopup as (): ImVec2
/** is mouse dragging? (uses io.MouseDraggingThreshold if lock_threshold < 0.0f) */
declare fn igIsMouseDragging as (button: ImGuiMouseButton, lock_threshold: Float = -1.0f): Bool
/** return the delta from the initial clicking position while the mouse button is pressed or was just released. This is locked and return 0.0f until the mouse moves past a distance threshold at least once (uses io.MouseDraggingThreshold if lock_threshold < 0.0f) */
declare fn igGetMouseDragDelta as (button: ImGuiMouseButton = 0, lock_threshold: Float = -1.0f): ImVec2
declare fn igResetMouseDragDelta as (button: ImGuiMouseButton = 0)
/** get desired mouse cursor shape. Important: reset in ImGui::NewFrame(), this is updated during the frame. valid before Render(). If you use software rendering by setting io.MouseDrawCursor ImGui will render those for you */
declare fn igGetMouseCursor as (): ImGuiMouseCursor
/** set desired mouse cursor shape */
declare fn igSetMouseCursor as (cursor_type: ImGuiMouseCursor)
/** Override io.WantCaptureMouse flag next frame (said flag is left for your application to handle, typical when true it instructs your app to ignore inputs). This is equivalent to setting "io.WantCaptureMouse = want_capture_mouse;" after the next NewFrame() call. */
declare fn igSetNextFrameWantCaptureMouse as (want_capture_mouse: Bool)
declare fn igGetClipboardText as (): CString?
declare fn igSetClipboardText as (text: CString)
/** call after CreateContext() and before the first call to NewFrame(). NewFrame() automatically calls LoadIniSettingsFromDisk(io.IniFilename). */
declare fn igLoadIniSettingsFromDisk as (ini_filename: CString)
/** call after CreateContext() and before the first call to NewFrame() to provide .ini data from your own data source. */
declare fn igLoadIniSettingsFromMemory as (ini_data: CString, ini_size: Size = 0)
/** this is automatically called (if io.IniFilename is not empty) a few seconds after any modification that should be reflected in the .ini file (and also by DestroyContext). */
declare fn igSaveIniSettingsToDisk as (ini_filename: CString)
/** return a zero-terminated string with the .ini data which you can save by your own mean. call when io.WantSaveIniSettings is set, then save data by your own mean and clear io.WantSaveIniSettings. */
declare fn igSaveIniSettingsToMemory as (out_ini_size: @Size? = null): CString
declare fn igDebugTextEncoding as (text: CString)
declare fn igDebugFlashStyleColor as (idx: ImGuiCol)
declare fn igDebugStartItemPicker as ()
/** This is called by IMGUI_CHECKVERSION() macro. */
declare fn igDebugCheckVersionAndDataLayout as (version_str: CString, sz_io: Size, sz_style: Size, sz_vec2: Size, sz_vec4: Size, sz_drawvert: Size, sz_drawidx: Size): Bool
/** Call via IMGUI_DEBUG_LOG() for maximum stripping in caller code! */
declare fn igDebugLog as (fmt: CString, ...)
declare fn igSetAllocatorFunctions as (alloc_func: ImGuiMemAllocFunc, free_func: ImGuiMemFreeFunc, user_data: @Any? = null)
declare fn igMemAlloc as (size: Size): @Any?
declare fn igMemFree as (ptr: @Any?)
/** A new ImGuiTableSortSpecs with ImGui's defaults, allocated by ImGui; free it with ImGuiTableSortSpecs_destroy. */
declare fn ImGuiTableSortSpecs_ImGuiTableSortSpecs as (): @ImGuiTableSortSpecs
/** Frees a ImGuiTableSortSpecs made by its constructor. */
declare fn ImGuiTableSortSpecs_destroy as (self: @ImGuiTableSortSpecs)
/** A new ImGuiTableColumnSortSpecs with ImGui's defaults, allocated by ImGui; free it with ImGuiTableColumnSortSpecs_destroy. */
declare fn ImGuiTableColumnSortSpecs_ImGuiTableColumnSortSpecs as (): @ImGuiTableColumnSortSpecs
/** Frees a ImGuiTableColumnSortSpecs made by its constructor. */
declare fn ImGuiTableColumnSortSpecs_destroy as (self: @ImGuiTableColumnSortSpecs)
/** A new ImGuiStyle with ImGui's defaults, allocated by ImGui; free it with ImGuiStyle_destroy. */
declare fn ImGuiStyle_ImGuiStyle as (): @ImGuiStyle
/** Frees a ImGuiStyle made by its constructor. */
declare fn ImGuiStyle_destroy as (self: @ImGuiStyle)
/** Scale all spacing/padding/thickness values. Do not scale fonts. See comments in definition. Consider not calling this if your initial scale factor if <1.0. */
declare fn ImGuiStyle_ScaleAllSizes as (self: @ImGuiStyle, scale_factor: Float)
/** Queue a new key down/up event. Key should be "translated" (as in, generally ImGuiKey_A matches the key end-user would use to emit an 'A' character) */
declare fn ImGuiIO_AddKeyEvent as (self: @ImGuiIO, key: ImGuiKey, down: Bool)
/** Queue a new key down/up event for analog values (e.g. ImGuiKey_Gamepad_ values). Dead-zones should be handled by the backend. */
declare fn ImGuiIO_AddKeyAnalogEvent as (self: @ImGuiIO, key: ImGuiKey, down: Bool, v: Float)
/** Queue a mouse position update. Use -FLT_MAX,-FLT_MAX to signify no mouse (e.g. app not focused and not hovered) */
declare fn ImGuiIO_AddMousePosEvent as (self: @ImGuiIO, x: Float, y: Float)
/** Queue a mouse button change */
declare fn ImGuiIO_AddMouseButtonEvent as (self: @ImGuiIO, button: Int, down: Bool)
/** Queue a mouse wheel update. wheel_y<0: scroll down, wheel_y>0: scroll up, wheel_x<0: scroll right, wheel_x>0: scroll left. */
declare fn ImGuiIO_AddMouseWheelEvent as (self: @ImGuiIO, wheel_x: Float, wheel_y: Float)
/** Queue a mouse source change (Mouse/TouchScreen/Pen) */
declare fn ImGuiIO_AddMouseSourceEvent as (self: @ImGuiIO, source: ImGuiMouseSource)
/** Queue a gain/loss of focus for the application (generally based on OS/platform focus of your window) */
declare fn ImGuiIO_AddFocusEvent as (self: @ImGuiIO, focused: Bool)
/** Queue a new character input */
declare fn ImGuiIO_AddInputCharacter as (self: @ImGuiIO, c: UInt)
/** Queue a new character input from a UTF-16 character, it can be a surrogate */
declare fn ImGuiIO_AddInputCharacterUTF16 as (self: @ImGuiIO, c: ImWchar16)
/** Queue a new characters input from a UTF-8 string */
declare fn ImGuiIO_AddInputCharactersUTF8 as (self: @ImGuiIO, str: CString)
/** [Optional] Specify index for legacy <1.87 IsKeyXXX() functions with native indices + specify native keycode, scancode. */
declare fn ImGuiIO_SetKeyEventNativeData as (self: @ImGuiIO, key: ImGuiKey, native_keycode: Int, native_scancode: Int, native_legacy_index: Int = -1)
/** Set master flag for accepting key/mouse/text events (default to true). Useful if you have native dialog boxes that are interrupting your application loop/refresh, and you want to disable events being queued while your app is frozen. */
declare fn ImGuiIO_SetAppAcceptingEvents as (self: @ImGuiIO, accepting_events: Bool)
/** Clear all incoming events. */
declare fn ImGuiIO_ClearEventsQueue as (self: @ImGuiIO)
/** Clear current keyboard/gamepad state + current frame text input buffer. Equivalent to releasing all keys/buttons. */
declare fn ImGuiIO_ClearInputKeys as (self: @ImGuiIO)
/** Clear current mouse state. */
declare fn ImGuiIO_ClearInputMouse as (self: @ImGuiIO)
/** A new ImGuiIO with ImGui's defaults, allocated by ImGui; free it with ImGuiIO_destroy. */
declare fn ImGuiIO_ImGuiIO as (): @ImGuiIO
/** Frees a ImGuiIO made by its constructor. */
declare fn ImGuiIO_destroy as (self: @ImGuiIO)
/** A new ImGuiInputTextCallbackData with ImGui's defaults, allocated by ImGui; free it with ImGuiInputTextCallbackData_destroy. */
declare fn ImGuiInputTextCallbackData_ImGuiInputTextCallbackData as (): @ImGuiInputTextCallbackData
/** Frees a ImGuiInputTextCallbackData made by its constructor. */
declare fn ImGuiInputTextCallbackData_destroy as (self: @ImGuiInputTextCallbackData)
declare fn ImGuiInputTextCallbackData_DeleteChars as (self: @ImGuiInputTextCallbackData, pos: Int, bytes_count: Int)
declare fn ImGuiInputTextCallbackData_InsertChars as (self: @ImGuiInputTextCallbackData, pos: Int, text: CString, text_end: CString? = null)
declare fn ImGuiInputTextCallbackData_SelectAll as (self: @ImGuiInputTextCallbackData)
declare fn ImGuiInputTextCallbackData_SetSelection as (self: @ImGuiInputTextCallbackData, s: Int, e: Int)
declare fn ImGuiInputTextCallbackData_ClearSelection as (self: @ImGuiInputTextCallbackData)
declare fn ImGuiInputTextCallbackData_HasSelection as (self: @ImGuiInputTextCallbackData): Bool
/** A new ImGuiPayload with ImGui's defaults, allocated by ImGui; free it with ImGuiPayload_destroy. */
declare fn ImGuiPayload_ImGuiPayload as (): @ImGuiPayload
/** Frees a ImGuiPayload made by its constructor. */
declare fn ImGuiPayload_destroy as (self: @ImGuiPayload)
declare fn ImGuiPayload_Clear as (self: @ImGuiPayload)
declare fn ImGuiPayload_IsDataType as (self: @ImGuiPayload, type_: CString): Bool
declare fn ImGuiPayload_IsPreview as (self: @ImGuiPayload): Bool
declare fn ImGuiPayload_IsDelivery as (self: @ImGuiPayload): Bool
/** A new ImGuiOnceUponAFrame with ImGui's defaults, allocated by ImGui; free it with ImGuiOnceUponAFrame_destroy. */
declare fn ImGuiOnceUponAFrame_ImGuiOnceUponAFrame as (): @ImGuiOnceUponAFrame
/** Frees a ImGuiOnceUponAFrame made by its constructor. */
declare fn ImGuiOnceUponAFrame_destroy as (self: @ImGuiOnceUponAFrame)
/** A new ImGuiTextFilter with ImGui's defaults, allocated by ImGui; free it with ImGuiTextFilter_destroy. */
declare fn ImGuiTextFilter_ImGuiTextFilter as (default_filter: CString = ""c): @ImGuiTextFilter
/** Frees a ImGuiTextFilter made by its constructor. */
declare fn ImGuiTextFilter_destroy as (self: @ImGuiTextFilter)
declare fn ImGuiTextFilter_PassFilter as (self: @ImGuiTextFilter, text: CString, text_end: CString? = null): Bool
/** Update internal data when filter changes */
declare fn ImGuiTextFilter_Build as (self: @ImGuiTextFilter)
/** Clear filter */
declare fn ImGuiTextFilter_Clear as (self: @ImGuiTextFilter)
/** Useful if you need e.g. an alternative code-path when there are no filters */
declare fn ImGuiTextFilter_IsActive as (self: @ImGuiTextFilter): Bool
declare fn ImGuiTextFilter_Draw as (self: @ImGuiTextFilter, label: CString = "Filter"c): Bool
declare fn ImGuiTextFilter_DrawWithHint as (self: @ImGuiTextFilter, label: CString = "Filter"c, hint: CString = "incl -excl"c): Bool
/** A new ImGuiTextBuffer with ImGui's defaults, allocated by ImGui; free it with ImGuiTextBuffer_destroy. */
declare fn ImGuiTextBuffer_ImGuiTextBuffer as (): @ImGuiTextBuffer
/** Frees a ImGuiTextBuffer made by its constructor. */
declare fn ImGuiTextBuffer_destroy as (self: @ImGuiTextBuffer)
declare fn ImGuiTextBuffer_begin as (self: @ImGuiTextBuffer): CString
/** Buf is zero-terminated, so end() will point on the zero-terminator */
declare fn ImGuiTextBuffer_end as (self: @ImGuiTextBuffer): CString
declare fn ImGuiTextBuffer_size as (self: @ImGuiTextBuffer): Int
declare fn ImGuiTextBuffer_empty as (self: @ImGuiTextBuffer): Bool
declare fn ImGuiTextBuffer_clear as (self: @ImGuiTextBuffer)
/** Similar to resize(0) on ImVector: empty string but don't free buffer. */
declare fn ImGuiTextBuffer_resize as (self: @ImGuiTextBuffer, size: Int)
declare fn ImGuiTextBuffer_reserve as (self: @ImGuiTextBuffer, capacity: Int)
declare fn ImGuiTextBuffer_c_str as (self: @ImGuiTextBuffer): CString
declare fn ImGuiTextBuffer_append as (self: @ImGuiTextBuffer, str: CString, str_end: CString? = null)
declare fn ImGuiTextBuffer_appendf as (self: @ImGuiTextBuffer, fmt: CString, ...)
/** A new ImGuiStoragePair with ImGui's defaults, allocated by ImGui; free it with ImGuiStoragePair_destroy. */
declare fn ImGuiStoragePair_ImGuiStoragePair_Int as (_key: ImGuiID, _val: Int): @ImGuiStoragePair
/** Frees a ImGuiStoragePair made by its constructor. */
declare fn ImGuiStoragePair_destroy as (self: @ImGuiStoragePair)
/** A new ImGuiStoragePair with ImGui's defaults, allocated by ImGui; free it with ImGuiStoragePair_destroy. */
declare fn ImGuiStoragePair_ImGuiStoragePair_Float as (_key: ImGuiID, _val: Float): @ImGuiStoragePair
/** A new ImGuiStoragePair with ImGui's defaults, allocated by ImGui; free it with ImGuiStoragePair_destroy. */
declare fn ImGuiStoragePair_ImGuiStoragePair_Ptr as (_key: ImGuiID, _val: @Any?): @ImGuiStoragePair
declare fn ImGuiStorage_Clear as (self: @ImGuiStorage)
declare fn ImGuiStorage_GetInt as (self: @ImGuiStorage, key: ImGuiID, default_val: Int = 0): Int
declare fn ImGuiStorage_SetInt as (self: @ImGuiStorage, key: ImGuiID, val_: Int)
declare fn ImGuiStorage_GetBool as (self: @ImGuiStorage, key: ImGuiID, default_val: Bool = false): Bool
declare fn ImGuiStorage_SetBool as (self: @ImGuiStorage, key: ImGuiID, val_: Bool)
declare fn ImGuiStorage_GetFloat as (self: @ImGuiStorage, key: ImGuiID, default_val: Float = 0.0f): Float
declare fn ImGuiStorage_SetFloat as (self: @ImGuiStorage, key: ImGuiID, val_: Float)
/** default_val is NULL */
declare fn ImGuiStorage_GetVoidPtr as (self: @ImGuiStorage, key: ImGuiID): @Any?
declare fn ImGuiStorage_SetVoidPtr as (self: @ImGuiStorage, key: ImGuiID, val_: @Any?)
declare fn ImGuiStorage_GetIntRef as (self: @ImGuiStorage, key: ImGuiID, default_val: Int = 0): Int[]c
declare fn ImGuiStorage_GetBoolRef as (self: @ImGuiStorage, key: ImGuiID, default_val: Bool = false): Bool[]c
declare fn ImGuiStorage_GetFloatRef as (self: @ImGuiStorage, key: ImGuiID, default_val: Float = 0.0f): Float[]c
declare fn ImGuiStorage_BuildSortByKey as (self: @ImGuiStorage)
declare fn ImGuiStorage_SetAllInt as (self: @ImGuiStorage, val_: Int)
/** A new ImGuiListClipper with ImGui's defaults, allocated by ImGui; free it with ImGuiListClipper_destroy. */
declare fn ImGuiListClipper_ImGuiListClipper as (): @ImGuiListClipper
/** Frees a ImGuiListClipper made by its constructor. */
declare fn ImGuiListClipper_destroy as (self: @ImGuiListClipper)
declare fn ImGuiListClipper_Begin as (self: @ImGuiListClipper, items_count: Int, items_height: Float = -1.0f)
/** Automatically called on the last call of Step() that returns false. */
declare fn ImGuiListClipper_End as (self: @ImGuiListClipper)
/** Call until it returns false. The DisplayStart/DisplayEnd fields will be set and you can process/draw those items. */
declare fn ImGuiListClipper_Step as (self: @ImGuiListClipper): Bool
declare fn ImGuiListClipper_IncludeItemByIndex as (self: @ImGuiListClipper, item_index: Int)
/** item_end is exclusive e.g. use (42, 42+1) to make item 42 never clipped. */
declare fn ImGuiListClipper_IncludeItemsByIndex as (self: @ImGuiListClipper, item_begin: Int, item_end: Int)
declare fn ImGuiListClipper_SeekCursorForItem as (self: @ImGuiListClipper, item_index: Int)
/** A new ImColor with ImGui's defaults, allocated by ImGui; free it with ImColor_destroy. */
declare fn ImColor_ImColor_Nil as (): @ImColor
/** Frees a ImColor made by its constructor. */
declare fn ImColor_destroy as (self: @ImColor)
/** A new ImColor with ImGui's defaults, allocated by ImGui; free it with ImColor_destroy. */
declare fn ImColor_ImColor_Float as (r: Float, g: Float, b: Float, a: Float = 1.0f): @ImColor
/** A new ImColor with ImGui's defaults, allocated by ImGui; free it with ImColor_destroy. */
declare fn ImColor_ImColor_Vec4 as (col: ImVec4): @ImColor
/** A new ImColor with ImGui's defaults, allocated by ImGui; free it with ImColor_destroy. */
declare fn ImColor_ImColor_Int as (r: Int, g: Int, b: Int, a: Int = 255): @ImColor
/** A new ImColor with ImGui's defaults, allocated by ImGui; free it with ImColor_destroy. */
declare fn ImColor_ImColor_U32 as (rgba: ImU32): @ImColor
declare fn ImColor_SetHSV as (self: @ImColor, h: Float, s: Float, v: Float, a: Float = 1.0f)
declare fn ImColor_HSV as (h: Float, s: Float, v: Float, a: Float = 1.0f): ImColor
/** A new ImGuiSelectionBasicStorage with ImGui's defaults, allocated by ImGui; free it with ImGuiSelectionBasicStorage_destroy. */
declare fn ImGuiSelectionBasicStorage_ImGuiSelectionBasicStorage as (): @ImGuiSelectionBasicStorage
/** Frees a ImGuiSelectionBasicStorage made by its constructor. */
declare fn ImGuiSelectionBasicStorage_destroy as (self: @ImGuiSelectionBasicStorage)
/** Apply selection requests coming from BeginMultiSelect() and EndMultiSelect() functions. It uses 'items_count' passed to BeginMultiSelect() */
declare fn ImGuiSelectionBasicStorage_ApplyRequests as (self: @ImGuiSelectionBasicStorage, ms_io: @ImGuiMultiSelectIO)
/** Query if an item id is in selection. */
declare fn ImGuiSelectionBasicStorage_Contains as (self: @ImGuiSelectionBasicStorage, id: ImGuiID): Bool
/** Clear selection */
declare fn ImGuiSelectionBasicStorage_Clear as (self: @ImGuiSelectionBasicStorage)
/** Swap two selections */
declare fn ImGuiSelectionBasicStorage_Swap as (self: @ImGuiSelectionBasicStorage, r: @ImGuiSelectionBasicStorage)
/** Add/remove an item from selection (generally done by ApplyRequests() function) */
declare fn ImGuiSelectionBasicStorage_SetItemSelected as (self: @ImGuiSelectionBasicStorage, id: ImGuiID, selected: Bool)
/** Convert index to item id based on provided adapter. */
declare fn ImGuiSelectionBasicStorage_GetStorageIdFromIndex as (self: @ImGuiSelectionBasicStorage, idx: Int): ImGuiID
/** A new ImGuiSelectionExternalStorage with ImGui's defaults, allocated by ImGui; free it with ImGuiSelectionExternalStorage_destroy. */
declare fn ImGuiSelectionExternalStorage_ImGuiSelectionExternalStorage as (): @ImGuiSelectionExternalStorage
/** Frees a ImGuiSelectionExternalStorage made by its constructor. */
declare fn ImGuiSelectionExternalStorage_destroy as (self: @ImGuiSelectionExternalStorage)
/** Apply selection requests by using AdapterSetItemSelected() calls */
declare fn ImGuiSelectionExternalStorage_ApplyRequests as (self: @ImGuiSelectionExternalStorage, ms_io: @ImGuiMultiSelectIO)
/** A new ImDrawCmd with ImGui's defaults, allocated by ImGui; free it with ImDrawCmd_destroy. */
declare fn ImDrawCmd_ImDrawCmd as (): @ImDrawCmd
/** Frees a ImDrawCmd made by its constructor. */
declare fn ImDrawCmd_destroy as (self: @ImDrawCmd)
/** == (TexRef._TexData ? TexRef._TexData->TexID : TexRef._TexID) */
declare fn ImDrawCmd_GetTexID as (self: @ImDrawCmd): ImTextureID
/** A new ImDrawListSplitter with ImGui's defaults, allocated by ImGui; free it with ImDrawListSplitter_destroy. */
declare fn ImDrawListSplitter_ImDrawListSplitter as (): @ImDrawListSplitter
/** Frees a ImDrawListSplitter made by its constructor. */
declare fn ImDrawListSplitter_destroy as (self: @ImDrawListSplitter)
/** Do not clear Channels[] so our allocations are reused next frame */
declare fn ImDrawListSplitter_Clear as (self: @ImDrawListSplitter)
declare fn ImDrawListSplitter_ClearFreeMemory as (self: @ImDrawListSplitter)
declare fn ImDrawListSplitter_Split as (self: @ImDrawListSplitter, draw_list: @ImDrawList, count: Int)
declare fn ImDrawListSplitter_Merge as (self: @ImDrawListSplitter, draw_list: @ImDrawList)
declare fn ImDrawListSplitter_SetCurrentChannel as (self: @ImDrawListSplitter, draw_list: @ImDrawList, channel_idx: Int)
/** A new ImDrawList with ImGui's defaults, allocated by ImGui; free it with ImDrawList_destroy. */
declare fn ImDrawList_ImDrawList as (shared_data: @ImDrawListSharedData): @ImDrawList
/** Frees a ImDrawList made by its constructor. */
declare fn ImDrawList_destroy as (self: @ImDrawList)
/** Render-level scissoring. This is passed down to your render function but not used for CPU-side coarse clipping. Prefer using higher-level ImGui::PushClipRect() to affect logic (hit-testing and widget culling) */
declare fn ImDrawList_PushClipRect as (self: @ImDrawList, clip_rect_min: ImVec2, clip_rect_max: ImVec2, intersect_with_current_clip_rect: Bool = false)
declare fn ImDrawList_PushClipRectFullScreen as (self: @ImDrawList)
declare fn ImDrawList_PopClipRect as (self: @ImDrawList)
declare fn ImDrawList_PushTexture as (self: @ImDrawList, tex_ref: ImTextureRef)
declare fn ImDrawList_PopTexture as (self: @ImDrawList)
/** [BETA] Please notify me if you are using this. */
declare fn ImDrawList_PushDrawFlag as (self: @ImDrawList, flags: ImDrawFlags, enabled: Bool)
declare fn ImDrawList_PopDrawFlag as (self: @ImDrawList)
declare fn ImDrawList_GetClipRectMin as (self: @ImDrawList): ImVec2
declare fn ImDrawList_GetClipRectMax as (self: @ImDrawList): ImVec2
declare fn ImDrawList_AddLine as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, col: ImU32, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddLineH as (self: @ImDrawList, min_x: Float, max_x: Float, y: Float, col: ImU32, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddLineV as (self: @ImDrawList, x: Float, min_y: Float, max_y: Float, col: ImU32, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
/** a: upper-left, b: lower-right (== upper-left + size) */
declare fn ImDrawList_AddRect as (self: @ImDrawList, p_min: ImVec2, p_max: ImVec2, col: ImU32, rounding: Float = 0.0f, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
/** a: upper-left, b: lower-right (== upper-left + size) */
declare fn ImDrawList_AddRectFilled as (self: @ImDrawList, p_min: ImVec2, p_max: ImVec2, col: ImU32, rounding: Float = 0.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddRectFilledMultiColor as (self: @ImDrawList, p_min: ImVec2, p_max: ImVec2, col_upr_left: ImU32, col_upr_right: ImU32, col_bot_right: ImU32, col_bot_left: ImU32)
declare fn ImDrawList_AddQuad as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, p3: ImVec2, p4: ImVec2, col: ImU32, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddQuadFilled as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, p3: ImVec2, p4: ImVec2, col: ImU32)
declare fn ImDrawList_AddTriangle as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, p3: ImVec2, col: ImU32, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddTriangleFilled as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, p3: ImVec2, col: ImU32)
declare fn ImDrawList_AddCircle as (self: @ImDrawList, center: ImVec2, radius: Float, col: ImU32, num_segments: Int = 0, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddCircleFilled as (self: @ImDrawList, center: ImVec2, radius: Float, col: ImU32, num_segments: Int = 0)
declare fn ImDrawList_AddNgon as (self: @ImDrawList, center: ImVec2, radius: Float, col: ImU32, num_segments: Int, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddNgonFilled as (self: @ImDrawList, center: ImVec2, radius: Float, col: ImU32, num_segments: Int)
declare fn ImDrawList_AddEllipse as (self: @ImDrawList, center: ImVec2, radius: ImVec2, col: ImU32, rot: Float = 0.0f, num_segments: Int = 0, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddEllipseFilled as (self: @ImDrawList, center: ImVec2, radius: ImVec2, col: ImU32, rot: Float = 0.0f, num_segments: Int = 0)
declare fn ImDrawList_AddText_Vec2 as (self: @ImDrawList, pos: ImVec2, col: ImU32, text_begin: CString, text_end: CString? = null)
declare fn ImDrawList_AddText extern "ImDrawList_AddText_Vec2" as (self: @ImDrawList, pos: ImVec2, col: ImU32, text_begin: CString, text_end: CString? = null)
declare fn ImDrawList_AddText_FontPtr as (self: @ImDrawList, font: @ImFont, font_size: Float, pos: ImVec2, col: ImU32, text_begin: CString, text_end: CString? = null, wrap_width: Float = 0.0f, cpu_fine_clip_rect: @ImVec4? = null)
declare fn ImDrawList_AddText extern "ImDrawList_AddText_FontPtr" as (self: @ImDrawList, font: @ImFont, font_size: Float, pos: ImVec2, col: ImU32, text_begin: CString, text_end: CString? = null, wrap_width: Float = 0.0f, cpu_fine_clip_rect: @ImVec4? = null)
/** Cubic Bezier (4 control points) */
declare fn ImDrawList_AddBezierCubic as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, p3: ImVec2, p4: ImVec2, col: ImU32, thickness: Float, num_segments: Int = 0, flags: ImDrawFlags = 0)
/** Quadratic Bezier (3 control points) */
declare fn ImDrawList_AddBezierQuadratic as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, p3: ImVec2, col: ImU32, thickness: Float, num_segments: Int = 0, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddPolyline as (self: @ImDrawList, points: ImVec2[]c, num_points: Int, col: ImU32, thickness: Float, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddConvexPolyFilled as (self: @ImDrawList, points: ImVec2[]c, num_points: Int, col: ImU32, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddConcavePolyFilled as (self: @ImDrawList, points: ImVec2[]c, num_points: Int, col: ImU32)
declare fn ImDrawList_AddImage as (self: @ImDrawList, tex_ref: ImTextureRef, p_min: ImVec2, p_max: ImVec2, uv_min: ImVec2 = ImVec2 { 0.0f, 0.0f }, uv_max: ImVec2 = ImVec2 { 1.0f, 1.0f }, col: ImU32 = 4294967295)
declare fn ImDrawList_AddImageQuad as (self: @ImDrawList, tex_ref: ImTextureRef, p1: ImVec2, p2: ImVec2, p3: ImVec2, p4: ImVec2, uv1: ImVec2 = ImVec2 { 0.0f, 0.0f }, uv2: ImVec2 = ImVec2 { 1.0f, 0.0f }, uv3: ImVec2 = ImVec2 { 1.0f, 1.0f }, uv4: ImVec2 = ImVec2 { 0.0f, 1.0f }, col: ImU32 = 4294967295)
declare fn ImDrawList_AddImageRounded as (self: @ImDrawList, tex_ref: ImTextureRef, p_min: ImVec2, p_max: ImVec2, uv_min: ImVec2, uv_max: ImVec2, col: ImU32, rounding: Float, flags: ImDrawFlags = 0)
declare fn ImDrawList_PathClear as (self: @ImDrawList)
declare fn ImDrawList_PathLineTo as (self: @ImDrawList, pos: ImVec2)
declare fn ImDrawList_PathLineToMergeDuplicate as (self: @ImDrawList, pos: ImVec2)
declare fn ImDrawList_PathFillConvex as (self: @ImDrawList, col: ImU32, flags: ImDrawFlags = 0)
declare fn ImDrawList_PathFillConcave as (self: @ImDrawList, col: ImU32)
declare fn ImDrawList_PathStroke as (self: @ImDrawList, col: ImU32, thickness: Float = 1.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_PathArcTo as (self: @ImDrawList, center: ImVec2, radius: Float, a_min: Float, a_max: Float, num_segments: Int = 0)
/** Use precomputed angles for a 12 steps circle */
declare fn ImDrawList_PathArcToFast as (self: @ImDrawList, center: ImVec2, radius: Float, a_min_of_12: Int, a_max_of_12: Int)
/** Ellipse */
declare fn ImDrawList_PathEllipticalArcTo as (self: @ImDrawList, center: ImVec2, radius: ImVec2, rot: Float, a_min: Float, a_max: Float, num_segments: Int = 0)
/** Cubic Bezier (4 control points) */
declare fn ImDrawList_PathBezierCubicCurveTo as (self: @ImDrawList, p2: ImVec2, p3: ImVec2, p4: ImVec2, num_segments: Int = 0)
/** Quadratic Bezier (3 control points) */
declare fn ImDrawList_PathBezierQuadraticCurveTo as (self: @ImDrawList, p2: ImVec2, p3: ImVec2, num_segments: Int = 0)
declare fn ImDrawList_PathRect as (self: @ImDrawList, rect_min: ImVec2, rect_max: ImVec2, rounding: Float = 0.0f, flags: ImDrawFlags = 0)
declare fn ImDrawList_AddCallback as (self: @ImDrawList, callback: ImDrawCallback, userdata: @Any? = null, userdata_size: Size = 0)
/** This is useful if you need to forcefully create a new draw call (to allow for dependent rendering / blending). Otherwise primitives are merged into the same draw-call as much as possible */
declare fn ImDrawList_AddDrawCmd as (self: @ImDrawList)
/** Create a clone of the CmdBuffer/IdxBuffer/VtxBuffer. For multi-threaded rendering, consider using `imgui_threaded_rendering` from https://github.com/ocornut/imgui_club instead. */
declare fn ImDrawList_CloneOutput as (self: @ImDrawList): @ImDrawList
declare fn ImDrawList_ChannelsSplit as (self: @ImDrawList, count: Int)
declare fn ImDrawList_ChannelsMerge as (self: @ImDrawList)
declare fn ImDrawList_ChannelsSetCurrent as (self: @ImDrawList, n: Int)
declare fn ImDrawList_PrimReserve as (self: @ImDrawList, idx_count: Int, vtx_count: Int)
declare fn ImDrawList_PrimUnreserve as (self: @ImDrawList, idx_count: Int, vtx_count: Int)
/** Axis aligned rectangle (composed of two triangles) */
declare fn ImDrawList_PrimRect as (self: @ImDrawList, a: ImVec2, b: ImVec2, col: ImU32)
declare fn ImDrawList_PrimRectUV as (self: @ImDrawList, a: ImVec2, b: ImVec2, uv_a: ImVec2, uv_b: ImVec2, col: ImU32)
declare fn ImDrawList_PrimQuadUV as (self: @ImDrawList, a: ImVec2, b: ImVec2, c: ImVec2, d: ImVec2, uv_a: ImVec2, uv_b: ImVec2, uv_c: ImVec2, uv_d: ImVec2, col: ImU32)
declare fn ImDrawList_PrimWriteVtx as (self: @ImDrawList, pos: ImVec2, uv: ImVec2, col: ImU32)
declare fn ImDrawList_PrimWriteIdx as (self: @ImDrawList, idx: ImDrawIdx)
/** Write vertex with unique index */
declare fn ImDrawList_PrimVtx as (self: @ImDrawList, pos: ImVec2, uv: ImVec2, col: ImU32)
declare fn ImDrawList__SetDrawListSharedData as (self: @ImDrawList, data: @ImDrawListSharedData)
declare fn ImDrawList__ResetForNewFrame as (self: @ImDrawList)
declare fn ImDrawList__SetPixelDensity as (self: @ImDrawList, pixel_density: Float)
declare fn ImDrawList__ClearFreeMemory as (self: @ImDrawList)
declare fn ImDrawList__PopUnusedDrawCmd as (self: @ImDrawList)
declare fn ImDrawList__TryMergeDrawCmds as (self: @ImDrawList)
declare fn ImDrawList__OnChangedClipRect as (self: @ImDrawList)
declare fn ImDrawList__OnChangedTexture as (self: @ImDrawList)
declare fn ImDrawList__OnChangedVtxOffset as (self: @ImDrawList)
declare fn ImDrawList__SetTexture as (self: @ImDrawList, tex_ref: ImTextureRef)
declare fn ImDrawList__CalcCircleAutoSegmentCount as (self: @ImDrawList, radius: Float): Int
declare fn ImDrawList__PathArcToFastEx as (self: @ImDrawList, center: ImVec2, radius: Float, a_min_sample: Int, a_max_sample: Int, a_step: Int)
declare fn ImDrawList__PathArcToN as (self: @ImDrawList, center: ImVec2, radius: Float, a_min: Float, a_max: Float, num_segments: Int)
declare fn ImDrawList__AddRectFilledBaked as (self: @ImDrawList, p_min: ImVec2, p_max: ImVec2, col: ImU32, r: Float, tex_uvs: ImVec4, flags: ImDrawFlags)
declare fn ImDrawList__AddRectBaked as (self: @ImDrawList, p_min: ImVec2, p_max: ImVec2, col: ImU32, r: Float, t: Float, tex_uvs: ImVec4, flags: ImDrawFlags)
declare fn ImDrawList__AddLine as (self: @ImDrawList, p1: ImVec2, p2: ImVec2, col: ImU32, thickness: Float, flags: ImDrawFlags)
declare fn ImDrawList__AddRectTinyRounding as (self: @ImDrawList, p_min: ImVec2, p_max: ImVec2, col: ImU32, rounding: Float, thickness: Float, flags: ImDrawFlags)
declare fn ImDrawList__SelectLineTexture as (self: @ImDrawList, screen_thickness: Float, out out_uv0: ImVec2, out out_uv1: ImVec2, out out_fringe: Float, flags: ImDrawFlags)
declare fn ImDrawList__CalculateCenterBiasedOffset as (self: @ImDrawList, thickness: Float): Float
declare fn ImDrawList__AddPolyline as (self: @ImDrawList, points: ImVec2[]c, num_points: Int, col: ImU32, thickness: Float, flags: ImDrawFlags, max_inner_offset: Float)
/** A new ImDrawData with ImGui's defaults, allocated by ImGui; free it with ImDrawData_destroy. */
declare fn ImDrawData_ImDrawData as (): @ImDrawData
/** Frees a ImDrawData made by its constructor. */
declare fn ImDrawData_destroy as (self: @ImDrawData)
declare fn ImDrawData_Clear as (self: @ImDrawData)
/** Helper to add an external draw list into an existing ImDrawData. */
declare fn ImDrawData_AddDrawList as (self: @ImDrawData, draw_list: @ImDrawList)
/** Helper to convert all buffers from indexed to non-indexed, in case you cannot render indexed. Note: this is slow and most likely a waste of resources. Always prefer indexed rendering! */
declare fn ImDrawData_DeIndexAllBuffers as (self: @ImDrawData)
/** Helper to scale the ClipRect field of each ImDrawCmd. Use if your final output buffer is at a different scale than Dear ImGui expects, or if there is a difference between your window resolution and framebuffer resolution. */
declare fn ImDrawData_ScaleClipRects as (self: @ImDrawData, fb_scale: ImVec2)
/** A new ImTextureData with ImGui's defaults, allocated by ImGui; free it with ImTextureData_destroy. */
declare fn ImTextureData_ImTextureData as (): @ImTextureData
/** Frees a ImTextureData made by its constructor. */
declare fn ImTextureData_destroy as (self: @ImTextureData)
declare fn ImTextureData_Create as (self: @ImTextureData, format: ImTextureFormat, w: Int, h: Int)
declare fn ImTextureData_DestroyPixels as (self: @ImTextureData)
declare fn ImTextureData_GetPixels as (self: @ImTextureData): @Any?
declare fn ImTextureData_GetPixelsAt as (self: @ImTextureData, x: Int, y: Int): @Any?
declare fn ImTextureData_GetSizeInBytes as (self: @ImTextureData): Int
declare fn ImTextureData_GetPitch as (self: @ImTextureData): Int
declare fn ImTextureData_GetTexRef as (self: @ImTextureData): ImTextureRef
declare fn ImTextureData_GetTexID as (self: @ImTextureData): ImTextureID
declare fn ImTextureData_SetTexID as (self: @ImTextureData, tex_id: ImTextureID)
declare fn ImTextureData_SetStatus as (self: @ImTextureData, status: ImTextureStatus)
/** A new ImFontConfig with ImGui's defaults, allocated by ImGui; free it with ImFontConfig_destroy. */
declare fn ImFontConfig_ImFontConfig as (): @ImFontConfig
/** Frees a ImFontConfig made by its constructor. */
declare fn ImFontConfig_destroy as (self: @ImFontConfig)
/** A new ImFontGlyph with ImGui's defaults, allocated by ImGui; free it with ImFontGlyph_destroy. */
declare fn ImFontGlyph_ImFontGlyph as (): @ImFontGlyph
/** Frees a ImFontGlyph made by its constructor. */
declare fn ImFontGlyph_destroy as (self: @ImFontGlyph)
/** A new ImFontGlyphRangesBuilder with ImGui's defaults, allocated by ImGui; free it with ImFontGlyphRangesBuilder_destroy. */
declare fn ImFontGlyphRangesBuilder_ImFontGlyphRangesBuilder as (): @ImFontGlyphRangesBuilder
/** Frees a ImFontGlyphRangesBuilder made by its constructor. */
declare fn ImFontGlyphRangesBuilder_destroy as (self: @ImFontGlyphRangesBuilder)
declare fn ImFontGlyphRangesBuilder_Clear as (self: @ImFontGlyphRangesBuilder)
/** Get bit n in the array */
declare fn ImFontGlyphRangesBuilder_GetBit as (self: @ImFontGlyphRangesBuilder, n: Size): Bool
/** Set bit n in the array */
declare fn ImFontGlyphRangesBuilder_SetBit as (self: @ImFontGlyphRangesBuilder, n: Size)
/** Add character */
declare fn ImFontGlyphRangesBuilder_AddChar as (self: @ImFontGlyphRangesBuilder, c: ImWchar)
/** Add string (each character of the UTF-8 string are added) */
declare fn ImFontGlyphRangesBuilder_AddText as (self: @ImFontGlyphRangesBuilder, text: CString, text_end: CString? = null)
/** Add ranges, e.g. builder.AddRanges(ImFontAtlas::GetGlyphRangesDefault()) to force add all of ASCII/Latin+Ext */
declare fn ImFontGlyphRangesBuilder_AddRanges as (self: @ImFontGlyphRangesBuilder, ranges: ImWchar[]c)
/** Output new ranges */
declare fn ImFontGlyphRangesBuilder_BuildRanges as (self: @ImFontGlyphRangesBuilder, out_ranges: @ImVector_ImWchar)
/** A new ImFontAtlasRect with ImGui's defaults, allocated by ImGui; free it with ImFontAtlasRect_destroy. */
declare fn ImFontAtlasRect_ImFontAtlasRect as (): @ImFontAtlasRect
/** Frees a ImFontAtlasRect made by its constructor. */
declare fn ImFontAtlasRect_destroy as (self: @ImFontAtlasRect)
/** A new ImFontAtlas with ImGui's defaults, allocated by ImGui; free it with ImFontAtlas_destroy. */
declare fn ImFontAtlas_ImFontAtlas as (): @ImFontAtlas
/** Frees a ImFontAtlas made by its constructor. */
declare fn ImFontAtlas_destroy as (self: @ImFontAtlas)
declare fn ImFontAtlas_AddFont as (self: @ImFontAtlas, font_cfg: @ImFontConfig): @ImFont?
/** Selects between AddFontDefaultVector() and AddFontDefaultBitmap(). */
declare fn ImFontAtlas_AddFontDefault as (self: @ImFontAtlas, font_cfg: @ImFontConfig? = null): @ImFont?
/** Embedded scalable font. Recommended at any higher size. */
declare fn ImFontAtlas_AddFontDefaultVector as (self: @ImFontAtlas, font_cfg: @ImFontConfig? = null): @ImFont?
/** Embedded classic pixel-clean font. Recommended at Size 13px with no scaling. */
declare fn ImFontAtlas_AddFontDefaultBitmap as (self: @ImFontAtlas, font_cfg: @ImFontConfig? = null): @ImFont?
declare fn ImFontAtlas_AddFontFromFileTTF as (self: @ImFontAtlas, filename: CString, size_pixels: Float = 0.0f, font_cfg: @ImFontConfig? = null, glyph_ranges: ImWchar[]c? = null): @ImFont?
/** Note: Transfer ownership of 'ttf_data' to ImFontAtlas! Will be deleted after destruction of the atlas. Set font_cfg->FontDataOwnedByAtlas=false to keep ownership of your data and it won't be freed. */
declare fn ImFontAtlas_AddFontFromMemoryTTF as (self: @ImFontAtlas, font_data: @Any?, font_data_size: Int, size_pixels: Float = 0.0f, font_cfg: @ImFontConfig? = null, glyph_ranges: ImWchar[]c? = null): @ImFont?
/** 'compressed_font_data' still owned by caller. Compress with binary_to_compressed_c.cpp. */
declare fn ImFontAtlas_AddFontFromMemoryCompressedTTF as (self: @ImFontAtlas, compressed_font_data: @Any?, compressed_font_data_size: Int, size_pixels: Float = 0.0f, font_cfg: @ImFontConfig? = null, glyph_ranges: ImWchar[]c? = null): @ImFont?
/** 'compressed_font_data_base85' still owned by caller. Compress with binary_to_compressed_c.cpp with -base85 parameter. */
declare fn ImFontAtlas_AddFontFromMemoryCompressedBase85TTF as (self: @ImFontAtlas, compressed_font_data_base85: CString, size_pixels: Float = 0.0f, font_cfg: @ImFontConfig? = null, glyph_ranges: ImWchar[]c? = null): @ImFont?
/** Remove a font */
declare fn ImFontAtlas_RemoveFont as (self: @ImFontAtlas, font: @ImFont)
/** Compact cached glyphs and texture. */
declare fn ImFontAtlas_CompactCache as (self: @ImFontAtlas)
/** Change font loader at runtime. */
declare fn ImFontAtlas_SetFontLoader as (self: @ImFontAtlas, font_loader: @ImFontLoader)
/** Clear everything (fonts + textures). Don't call mid-frame! */
declare fn ImFontAtlas_Clear as (self: @ImFontAtlas)
/** Clear input+output font data/glyphs. New fonts and textures will be recreated afterwards. */
declare fn ImFontAtlas_ClearFonts as (self: @ImFontAtlas)
/** [OBSOLETE] Clear input data (all ImFontConfig structures including sizes, TTF data, glyph ranges, etc.) = all the data used to build the texture and fonts. */
declare fn ImFontAtlas_ClearInputData as (self: @ImFontAtlas)
/** [OBSOLETE] Clear CPU-side copy of the texture data. Saves RAM once the texture has been copied to graphics memory. */
declare fn ImFontAtlas_ClearTexData as (self: @ImFontAtlas)
/** Basic Latin, Extended Latin */
declare fn ImFontAtlas_GetGlyphRangesDefault as (self: @ImFontAtlas): ImWchar[]c
/** Register a rectangle. Return -1 (ImFontAtlasRectId_Invalid) on error. */
declare fn ImFontAtlas_AddCustomRect as (self: @ImFontAtlas, width: Int, height: Int, out_r: @ImFontAtlasRect? = null): ImFontAtlasRectId
/** Unregister a rectangle. Existing pixels will stay in texture until resized / garbage collected. */
declare fn ImFontAtlas_RemoveCustomRect as (self: @ImFontAtlas, id: ImFontAtlasRectId)
/** Get rectangle coordinates for current texture. Valid immediately, never store this (read above)! */
declare fn ImFontAtlas_GetCustomRect as (self: @ImFontAtlas, id: ImFontAtlasRectId, out_r: @ImFontAtlasRect): Bool
/** A new ImFontBaked with ImGui's defaults, allocated by ImGui; free it with ImFontBaked_destroy. */
declare fn ImFontBaked_ImFontBaked as (): @ImFontBaked
/** Frees a ImFontBaked made by its constructor. */
declare fn ImFontBaked_destroy as (self: @ImFontBaked)
declare fn ImFontBaked_ClearOutputData as (self: @ImFontBaked)
/** Return U+FFFD glyph if requested glyph doesn't exists. */
declare fn ImFontBaked_FindGlyph as (self: @ImFontBaked, c: ImWchar): @ImFontGlyph?
/** Return NULL if glyph doesn't exist */
declare fn ImFontBaked_FindGlyphNoFallback as (self: @ImFontBaked, c: ImWchar): @ImFontGlyph?
declare fn ImFontBaked_GetCharAdvance as (self: @ImFontBaked, c: ImWchar): Float
declare fn ImFontBaked_IsGlyphLoaded as (self: @ImFontBaked, c: ImWchar): Bool
/** A new ImFont with ImGui's defaults, allocated by ImGui; free it with ImFont_destroy. */
declare fn ImFont_ImFont as (): @ImFont
/** Frees a ImFont made by its constructor. */
declare fn ImFont_destroy as (self: @ImFont)
declare fn ImFont_IsGlyphInFont as (self: @ImFont, c: ImWchar): Bool
declare fn ImFont_IsLoaded as (self: @ImFont): Bool
/** Fill ImFontConfig::Name. */
declare fn ImFont_GetDebugName as (self: @ImFont): CString
/** Get or create baked data for given size */
declare fn ImFont_GetFontBaked as (self: @ImFont, font_size: Float, density: Float = -1.0f): @ImFontBaked
declare fn ImFont_CalcTextSizeA as (self: @ImFont, size: Float, max_width: Float, wrap_width: Float, text_begin: CString, text_end: CString? = null, out_remaining: @CString? = null): ImVec2
declare fn ImFont_CalcWordWrapPosition as (self: @ImFont, size: Float, text: CString, text_end: CString, wrap_width: Float): CString
declare fn ImFont_RenderChar as (self: @ImFont, draw_list: @ImDrawList, size: Float, pos: ImVec2, col: ImU32, c: ImWchar, cpu_fine_clip: @ImVec4? = null)
declare fn ImFont_RenderText as (self: @ImFont, draw_list: @ImDrawList, size: Float, pos: ImVec2, col: ImU32, clip_rect: ImVec4, text_begin: CString, text_end: CString, wrap_width: Float = 0.0f, flags: ImDrawTextFlags = 0)
declare fn ImFont_ClearOutputData as (self: @ImFont)
/** Makes 'from_codepoint' character points to 'to_codepoint' glyph. */
declare fn ImFont_AddRemapCodepoint as (self: @ImFont, from_codepoint: ImWchar, to_codepoint: ImWchar)
declare fn ImFont_AddRemapCodepointToGlyphIndex as (self: @ImFont, from_codepoint: ImWchar, font_src_idx: Int, glyph_idx: Int)
declare fn ImFont_IsGlyphRangeUnused as (self: @ImFont, c_begin: UInt, c_last: UInt): Bool
/** A new ImGuiViewport with ImGui's defaults, allocated by ImGui; free it with ImGuiViewport_destroy. */
declare fn ImGuiViewport_ImGuiViewport as (): @ImGuiViewport
/** Frees a ImGuiViewport made by its constructor. */
declare fn ImGuiViewport_destroy as (self: @ImGuiViewport)
declare fn ImGuiViewport_GetCenter as (self: @ImGuiViewport): ImVec2
declare fn ImGuiViewport_GetWorkCenter as (self: @ImGuiViewport): ImVec2
/** A new ImGuiPlatformIO with ImGui's defaults, allocated by ImGui; free it with ImGuiPlatformIO_destroy. */
declare fn ImGuiPlatformIO_ImGuiPlatformIO as (): @ImGuiPlatformIO
/** Frees a ImGuiPlatformIO made by its constructor. */
declare fn ImGuiPlatformIO_destroy as (self: @ImGuiPlatformIO)
/** Clear all Platform_XXX fields. Typically called on Platform Backend shutdown. */
declare fn ImGuiPlatformIO_ClearPlatformHandlers as (self: @ImGuiPlatformIO)
/** Clear all Renderer_XXX fields. Typically called on Renderer Backend shutdown. */
declare fn ImGuiPlatformIO_ClearRendererHandlers as (self: @ImGuiPlatformIO)
/** A new ImGuiPlatformImeData with ImGui's defaults, allocated by ImGui; free it with ImGuiPlatformImeData_destroy. */
declare fn ImGuiPlatformImeData_ImGuiPlatformImeData as (): @ImGuiPlatformImeData
/** Frees a ImGuiPlatformImeData made by its constructor. */
declare fn ImGuiPlatformImeData_destroy as (self: @ImGuiPlatformImeData)
