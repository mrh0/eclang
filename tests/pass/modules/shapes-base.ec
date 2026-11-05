module "shapesbase"

rec Shape as
    sides: Int

rec Named extends Shape as
    name: String
