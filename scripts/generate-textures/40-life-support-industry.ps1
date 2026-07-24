param(
    [string]$OutputRoot = (Join-Path $PSScriptRoot "../../build/generated/resources/main/assets/fluxmachines/textures")
)

. (Join-Path $PSScriptRoot "00-texture-library.ps1") -OutputRoot $OutputRoot

$assetRoot = Split-Path $OutputRoot -Parent
$resourceRoot = Split-Path (Split-Path $assetRoot -Parent) -Parent
$dataRoot = Join-Path $resourceRoot "data"
$itemDir = Join-Path $OutputRoot "item"
$blockDir = Join-Path $OutputRoot "block"
$guiDir = Join-Path $OutputRoot "gui"

function Write-GeneratedFile([string]$Path, [string]$Content)
{
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $Path) | Out-Null
    Set-Content -LiteralPath $Path -Value $Content -Encoding utf8NoBOM
}

function Write-Json([string]$Path, $Value)
{
    Write-GeneratedFile $Path ($Value | ConvertTo-Json -Depth 16 -Compress)
}

function New-ResourceTexture([System.Drawing.Color]$Dark, [System.Drawing.Color]$Base, [System.Drawing.Color]$Light)
{
    $image = New-Texture $color.Clear
    Set-Rectangle $image 3 4 10 9 $color.Ink
    Set-Rectangle $image 4 3 8 9 $Dark
    Set-Rectangle $image 5 4 6 7 $Base
    Set-Points $image $Light @(@(6, 4), @(9, 5), @(5, 7), @(8, 8), @(10, 10))
    return $image
}

function New-OreTexture([System.Drawing.Color]$Stone, [System.Drawing.Color]$Dark, [System.Drawing.Color]$Bright)
{
    $image = New-Texture $Stone
    Set-Points $image $color.Ink @(@(2, 3), @(8, 2), @(13, 4), @(5, 7), @(10, 8), @(2, 12), @(7, 13), @(13, 11))
    Set-Points $image $Dark @(@(3, 3), @(8, 3), @(12, 5), @(5, 8), @(10, 9), @(3, 12), @(8, 12), @(12, 11))
    Set-Points $image $Bright @(@(4, 3), @(9, 3), @(12, 6), @(6, 8), @(11, 9), @(4, 12), @(9, 12))
    return $image
}

function New-ComponentTexture([System.Drawing.Color]$Accent, [System.Drawing.Color]$Glow)
{
    $image = New-Texture $color.Clear
    Set-Rectangle $image 2 3 12 10 $color.Ink
    Set-Rectangle $image 3 4 10 8 $color.Steel
    Set-Rectangle $image 4 5 8 6 $color.Panel
    Set-Rectangle $image 5 6 6 4 $Accent
    Set-Rectangle $image 7 7 2 2 $Glow
    Set-Points $image $color.Amber @(@(2, 5), @(13, 5), @(2, 10), @(13, 10))
    return $image
}

$metalColors = @{
    titanium = @([System.Drawing.Color]::FromArgb(255, 78, 98, 112),[System.Drawing.Color]::FromArgb(255, 150, 173, 187),[System.Drawing.Color]::FromArgb(255, 226, 239, 244))
    cobalt = @([System.Drawing.Color]::FromArgb(255, 28, 54, 112),[System.Drawing.Color]::FromArgb(255, 48, 102, 194),[System.Drawing.Color]::FromArgb(255, 117, 176, 255))
    tungsten = @([System.Drawing.Color]::FromArgb(255, 42, 42, 47),[System.Drawing.Color]::FromArgb(255, 83, 87, 96),[System.Drawing.Color]::FromArgb(255, 151, 157, 169))
    osmium = @([System.Drawing.Color]::FromArgb(255, 48, 67, 108),[System.Drawing.Color]::FromArgb(255, 105, 137, 194),[System.Drawing.Color]::FromArgb(255, 184, 213, 255))
    iridium = @([System.Drawing.Color]::FromArgb(255, 85, 52, 112),[System.Drawing.Color]::FromArgb(255, 172, 117, 205),[System.Drawing.Color]::FromArgb(255, 244, 203, 255))
}

foreach ($metal in $metalColors.Keys)
{
    $colors = $metalColors[$metal]
    Save-Texture (New-ResourceTexture $colors[0] $colors[1] $colors[2]) (Join-Path $itemDir "raw_$metal.png")
    Save-Texture (New-ResourceTexture $colors[0] $colors[1] $color.Amber) (Join-Path $itemDir "crushed_$metal.png")
    Save-Texture (New-ResourceTexture $colors[0] $colors[1] $colors[2]) (Join-Path $itemDir "purified_${metal}_dust.png")
    Save-Texture (New-ResourceTexture $colors[0] $colors[1] $colors[2]) (Join-Path $itemDir "${metal}_ingot.png")
}

Save-Texture (New-OreTexture $color.Steel $metalColors.titanium[1] $metalColors.titanium[2]) (Join-Path $blockDir "titanium_ore.png")
Save-Texture (New-OreTexture $color.Graphite $metalColors.titanium[1] $metalColors.titanium[2]) (Join-Path $blockDir "deepslate_titanium_ore.png")
Save-Texture (New-OreTexture $color.Steel $metalColors.cobalt[1] $metalColors.cobalt[2]) (Join-Path $blockDir "cobalt_ore.png")
Save-Texture (New-OreTexture $color.Graphite $metalColors.cobalt[1] $metalColors.cobalt[2]) (Join-Path $blockDir "deepslate_cobalt_ore.png")
Save-Texture (New-OreTexture $color.RedDark $metalColors.tungsten[1] $metalColors.tungsten[2]) (Join-Path $blockDir "tungsten_ore.png")
Save-Texture (New-OreTexture $color.RedDark $metalColors.osmium[1] $metalColors.osmium[2]) (Join-Path $blockDir "osmium_ore.png")
Save-Texture (New-OreTexture $color.Shadow $metalColors.iridium[1] $metalColors.iridium[2]) (Join-Path $blockDir "iridium_ore.png")

$components = @{
    sulfur_dust = @($color.Amber, $color.Glow); diamond_dust = @($color.Cyan, $color.Glow)
    bio_inert_alloy = @([System.Drawing.Color]::FromArgb(255, 63, 188, 132), $color.Glow)
    tungsten_osmium_alloy = @($metalColors.osmium[1], $metalColors.tungsten[2])
    iridium_superconductor = @($metalColors.iridium[1], $color.CyanLight)
    corrosion_resistant_casing = @($color.Nickel, $color.Amber); electrolytic_cell = @($color.Cyan, $color.Glow)
    plasma_coil = @($color.Red, $color.Amber); quantum_processor = @($metalColors.iridium[1], $color.Glow)
    diamond_capacitor_matrix = @($color.Cyan, $color.Glow); netherite_pressure_frame = @($color.Graphite, $color.CyanLight)
    stellar_regulation_core = @($color.Amber, $color.Glow); first_aid_nanite_matrix = @([System.Drawing.Color]::FromArgb(255, 70, 214, 131), $color.Glow)
    nutrition_recycling_module = @([System.Drawing.Color]::FromArgb(255, 232, 153, 45), $color.Glow)
    force_field_matrix = @($color.Cyan, $color.CyanLight); life_monitoring_core = @([System.Drawing.Color]::FromArgb(255, 221, 79, 115), $color.Glow)
    iridium_ring_frame = @($metalColors.iridium[1], $color.CyanLight); electric_life_support_ring = @([System.Drawing.Color]::FromArgb(255, 61, 210, 126), $color.Glow)
}
foreach ($name in $components.Keys)
{
    Save-Texture (New-ComponentTexture $components[$name][0] $components[$name][1]) (Join-Path $itemDir "$name.png")
}

$machines = @{
    resonant_crusher = $color.Cyan
    leaching_reactor = $color.Amber
    electrolytic_purifier = [System.Drawing.Color]::FromArgb(255, 85, 126, 238)
    plasma_furnace = $color.Red
    quantum_assembler = [System.Drawing.Color]::FromArgb(255, 198, 83, 239)
}

function New-MachineSurface([string]$Name, [string]$Surface)
{
    $accent = $machines[$Name]
    $image = New-Texture $color.Steel
    Add-CasingFrame $image $color.Steel $color.Light $color.Graphite
    Set-Rectangle $image 2 2 12 12 $color.Panel

    switch ($Surface)
    {
        "bottom" {
            Set-Rectangle $image 3 12 10 1 $color.Graphite
            Set-Rectangle $image 4 13 2 1 $color.Ink
            Set-Rectangle $image 10 13 2 1 $color.Ink
        }
        "side" {
            Set-Rectangle $image 3 3 3 10 $color.Graphite
            Set-Rectangle $image 7 3 6 10 $color.Shadow
            Set-Rectangle $image 8 4 1 8 $accent
            Set-Rectangle $image 10 4 1 8 $color.Steel
            Set-Points $image $color.Light @(@(4, 4), @(4, 7), @(4, 10), @(12, 5), @(12, 9))
        }
        "top" {
            Set-Rectangle $image 3 3 10 10 $color.Graphite
            Set-Rectangle $image 4 4 8 8 $color.Panel
            Set-Rectangle $image 5 5 6 1 $accent
            Set-Rectangle $image 5 10 6 1 $accent
            Set-Rectangle $image 7 7 2 2 $color.Glow
        }
        default {
            switch ($Name)
            {
                "resonant_crusher" {
                    # 双偏心转子与波纹共振腔，识别为粉碎设备。
                    Set-Rectangle $image 3 3 10 3 $color.Graphite
                    Set-Rectangle $image 4 4 8 1 $accent
                    Set-Rectangle $image 3 7 10 5 $color.Shadow
                    Set-Rectangle $image 4 8 3 3 $color.CyanDark
                    Set-Rectangle $image 9 8 3 3 $color.CyanDark
                    Set-Rectangle $image 5 9 1 1 $color.Glow
                    Set-Rectangle $image 10 9 1 1 $color.Glow
                    Set-Points $image $accent @(@(3, 7), @(4, 6), @(7, 6), @(8, 6), @(11, 6), @(12, 7), @(7, 11), @(8, 11))
                }
                "leaching_reactor" {
                    # 琥珀色耐压釜、压力表及酸液管线。
                    Set-Rectangle $image 5 3 6 10 $color.Graphite
                    Set-Rectangle $image 6 4 4 8 $color.RedDark
                    Set-Rectangle $image 7 5 2 5 $accent
                    Set-Rectangle $image 3 5 2 6 $color.Nickel
                    Set-Rectangle $image 11 5 2 6 $color.Nickel
                    Set-Rectangle $image 3 6 10 1 $color.Amber
                    Set-Rectangle $image 6 3 4 1 $color.Light
                    Set-Points $image $color.Glow @(@(7, 5), @(8, 5), @(7, 9), @(8, 9))
                }
                "electrolytic_purifier" {
                    # 三段式电解槽，蓝色母线贯穿电极板。
                    Set-Rectangle $image 3 3 10 10 $color.Graphite
                    Set-Rectangle $image 4 4 2 8 $accent
                    Set-Rectangle $image 7 4 2 8 $accent
                    Set-Rectangle $image 10 4 2 8 $accent
                    Set-Rectangle $image 3 5 10 1 $color.CyanLight
                    Set-Rectangle $image 3 10 10 1 $color.CyanDark
                    Set-Points $image $color.Glow @(@(4, 7), @(5, 8), @(7, 7), @(8, 8), @(10, 7), @(11, 8))
                }
                "plasma_furnace" {
                    # 真空炉门以圆形等离子弧和红热隔离环表现。
                    Set-Rectangle $image 4 3 8 10 $color.Graphite
                    Set-Rectangle $image 5 4 6 8 $color.RedDark
                    Set-Rectangle $image 6 5 4 6 $accent
                    Set-Rectangle $image 7 6 2 4 $color.Amber
                    Set-Rectangle $image 5 3 6 1 $color.Light
                    Set-Rectangle $image 5 12 6 1 $color.Ink
                    Set-Points $image $color.Glow @(@(6, 5), @(9, 5), @(6, 10), @(9, 10), @(7, 7), @(8, 8))
                }
                "quantum_assembler" {
                    # 紫色量子核心、四角导轨与金色装配触点。
                    Set-Rectangle $image 3 3 10 10 $color.Graphite
                    Set-Rectangle $image 4 4 2 2 $accent
                    Set-Rectangle $image 10 4 2 2 $accent
                    Set-Rectangle $image 4 10 2 2 $accent
                    Set-Rectangle $image 10 10 2 2 $accent
                    Set-Rectangle $image 6 6 4 4 $color.Shadow
                    Set-Rectangle $image 7 7 2 2 $color.Glow
                    Set-Points $image $color.Amber @(@(6, 5), @(9, 5), @(5, 6), @(10, 6), @(5, 9), @(10, 9), @(6, 10), @(9, 10))
                }
            }
        }
    }
    return $image
}

function New-MachineModelElement([int[]]$From, [int[]]$To, [string]$Texture)
{
    $faces = @{ }
    foreach ($face in @("down", "up", "north", "south", "west", "east"))
    {
        $faces[$face] = @{ uv = @(0, 0, 16, 16); texture = "#$Texture" }
    }
    return @{
    from = $From; to = $To; faces = $faces
}
}

function Write-MachineModel([string]$Name)
{
$elements = @(
@{
from = @(0, 0, 0); to = @(16, 16, 16); faces = @{
down = @{
uv = @(0, 0, 16, 16); texture = "#bottom"
}
up = @{
uv = @(0, 0, 16, 16); texture = "#top"
}
north = @{
uv = @(0, 0, 16, 16); texture = "#front"
}
south = @{
uv = @(0, 0, 16, 16); texture = "#back"
}
west = @{
uv = @(0, 0, 16, 16); texture = "#side"
}
east = @{
uv = @(0, 0, 16, 16); texture = "#side"
}
}
}
)

switch ($Name) {
"resonant_crusher" {
$elements += New-MachineModelElement @(2, 10, -2) @(14, 16, 1) "front"
}
"leaching_reactor" {
$elements += New-MachineModelElement @(5, 2, -2) @(11, 15, 1) "front"
}
"electrolytic_purifier" {
$elements += New-MachineModelElement @(2, 3, -1) @(14, 13, 0) "front"
}
"plasma_furnace" {
$elements += New-MachineModelElement @(5, 15, 5) @(11, 20, 11) "top"
}
"quantum_assembler" {
$elements += New-MachineModelElement @(2, 15, 2) @(4, 20, 4) "top"
$elements += New-MachineModelElement @(12, 15, 2) @(14, 20, 4) "top"
$elements += New-MachineModelElement @(2, 15, 12) @(4, 20, 14) "top"
$elements += New-MachineModelElement @(12, 15, 12) @(14, 20, 14) "top"
}
}

$model = @{
textures = @{
bottom = "fluxmachines:block/${Name}_bottom"; top = "fluxmachines:block/${Name}_top"; front = "fluxmachines:block/${Name}_front"; back = "fluxmachines:block/${Name}_side"; side = "fluxmachines:block/${Name}_side"
}; elements = $elements }
Write-Json (Join-Path $assetRoot "models/block/$Name.json") $model
}

foreach ($name in $machines.Keys)
{
Save-Texture (New-MachineSurface $name "front") (Join-Path $blockDir "${name}_front.png")
Save-Texture (New-MachineSurface $name "side") (Join-Path $blockDir "${name}_side.png")
Save-Texture (New-MachineSurface $name "top") (Join-Path $blockDir "${name}_top.png")
Save-Texture (New-MachineSurface $name "bottom") (Join-Path $blockDir "${name}_bottom.png")
}

$gui = New-Bitmap 176 186 $color.GuiBack
Set-Rectangle $gui 0 0 176 186 $color.GuiDark
Set-Rectangle $gui 1 1 174 184 $color.GuiBack
Set-Rectangle $gui 3 3 170 2 $color.Cyan
for ($row = 0; $row -lt 3; $row++) {
for ($column = 0; $column -lt 3; $column++) {
Add-GuiSlot $gui (43 + $column * 18) (29 + $row * 18)
}
}
Add-GuiSlot $gui 125 38
Add-GuiSlot $gui 143 38
for ($tank = 0; $tank -lt 4; $tank++) {
Add-GuiInset $gui (5 + $tank * 8) 34 7 46
}
Add-GuiInset $gui 87 47 30 8
Add-GuiInset $gui 160 18 11 70
for ($row = 0; $row -lt 3; $row++) {
for ($column = 0; $column -lt 9; $column++) {
Add-GuiSlot $gui (7 + $column * 18) (103 + $row * 18)
}
}
for ($column = 0; $column -lt 9; $column++) {
Add-GuiSlot $gui (7 + $column * 18) 161
}
Save-Texture $gui (Join-Path $guiDir "advanced_machine.png")

$simpleItems = @()
foreach ($metal in $metalColors.Keys)
{
$simpleItems += "raw_$metal", "crushed_$metal", "purified_${metal}_dust", "${metal}_ingot"
}
$simpleItems += $components.Keys
foreach ($name in $simpleItems)
{
Write-Json (Join-Path $assetRoot "models/item/$name.json") @{
parent = "item/generated"; textures = @{
layer0 = "fluxmachines:item/$name"
}
}
}

$oreBlocks = @("titanium_ore", "deepslate_titanium_ore", "cobalt_ore", "deepslate_cobalt_ore", "tungsten_ore", "osmium_ore", "iridium_ore")
$machineBlocks = @($machines.Keys)
foreach ($name in $oreBlocks + $machineBlocks)
{
Write-Json (Join-Path $assetRoot "blockstates/$name.json") @{
variants = @{
"" = @{
model = "fluxmachines:block/$name"
}
}
}
if ($name -in $oreBlocks) {
Write-Json (Join-Path $assetRoot "models/block/$name.json") @{
parent = "minecraft:block/cube_all"; textures = @{
all = "fluxmachines:block/$name"
}
}
} else {
Write-MachineModel $name
}
Write-Json (Join-Path $assetRoot "models/item/$name.json") @{
parent = "fluxmachines:block/$name"
}
}

foreach ($fluidName in @("industrial_acid", "spent_acid", "cryogenic_coolant", "nutrient_gel", "titanium_slurry", "cobalt_slurry", "tungsten_slurry", "osmium_slurry", "iridium_slurry"))
{
Write-Json (Join-Path $assetRoot "blockstates/$fluidName.json") @{
variants = @{
"" = @{
model = "forge:fluid"
}
}
}
Write-Json (Join-Path $assetRoot "models/item/${fluidName}_bucket.json") @{
parent = "forge:item/bucket"; fluid = "fluxmachines:$fluidName"
}
}

function Write-SelfLoot([string]$Name)
{
Write-Json (Join-Path $dataRoot "fluxmachines/loot_tables/blocks/$Name.json") @{
type = "minecraft:block"; pools = @(@{
rolls = 1; entries = @(@{
type = "minecraft:item"; name = "fluxmachines:$Name"
}); conditions = @(@{
condition = "minecraft:survives_explosion"
})
})
}
}
foreach ($name in $machineBlocks)
{
Write-SelfLoot $name
}

function Write-OreLoot([string]$BlockName, [string]$RawName)
{
$silk = @{
condition = "minecraft:match_tool"; predicate = @{
enchantments = @(@{
enchantment = "minecraft:silk_touch"; levels = @{
min = 1
}
})
}
}
$normal = @{
type = "minecraft:item"; name = "fluxmachines:$RawName"; functions = @(@{
function = "minecraft:apply_bonus"; enchantment = "minecraft:fortune"; formula = "minecraft:ore_drops"
}, @{
function = "minecraft:explosion_decay"
})
}
$entry = @{
type = "minecraft:alternatives"; children = @(@{
type = "minecraft:item"; name = "fluxmachines:$BlockName"; conditions = @($silk)
}, $normal)
}
Write-Json (Join-Path $dataRoot "fluxmachines/loot_tables/blocks/$BlockName.json") @{
type = "minecraft:block"; pools = @(@{
rolls = 1; entries = @($entry)
})
}
}
Write-OreLoot "titanium_ore" "raw_titanium"; Write-OreLoot "deepslate_titanium_ore" "raw_titanium"
Write-OreLoot "cobalt_ore" "raw_cobalt"; Write-OreLoot "deepslate_cobalt_ore" "raw_cobalt"
Write-OreLoot "tungsten_ore" "raw_tungsten"; Write-OreLoot "osmium_ore" "raw_osmium"; Write-OreLoot "iridium_ore" "raw_iridium"

Write-Json (Join-Path $dataRoot "minecraft/tags/blocks/needs_diamond_tool.json") @{
replace = $false; values = $oreBlocks + $machineBlocks
}

foreach ($metal in $metalColors.Keys)
{
$oreNames = if ($metal -eq "titanium" -or $metal -eq "cobalt")
{
@("fluxmachines:${metal}_ore", "fluxmachines:deepslate_${metal}_ore")
}
else
{
@("fluxmachines:${metal}_ore")
}
Write-Json (Join-Path $dataRoot "forge/tags/blocks/ores/$metal.json") @{
replace = $false; values = $oreNames
}
Write-Json (Join-Path $dataRoot "forge/tags/items/ores/$metal.json") @{
replace = $false; values = $oreNames
}
Write-Json (Join-Path $dataRoot "forge/tags/items/raw_materials/$metal.json") @{
replace = $false; values = @("fluxmachines:raw_$metal")
}
Write-Json (Join-Path $dataRoot "forge/tags/items/dusts/$metal.json") @{
replace = $false; values = @("fluxmachines:purified_${metal}_dust")
}
Write-Json (Join-Path $dataRoot "forge/tags/items/ingots/$metal.json") @{
replace = $false; values = @("fluxmachines:${metal}_ingot")
}
}

function Write-OreWorldgen([string]$Name, [int]$Size, [int]$Count, [int]$MinY, [int]$MaxY, [string]$TargetTag, [string]$State, $Biomes)
{
$target = @{
target = @{
predicate_type = "minecraft:tag_match"; tag = $TargetTag
}; state = @{
Name = "fluxmachines:$State"
}
}
Write-Json (Join-Path $dataRoot "fluxmachines/worldgen/configured_feature/ore_$Name.json") @{
type = "minecraft:ore"; config = @{
size = $Size; discard_chance_on_air_exposure = 0.0; targets = @($target)
}
}
$height = @{
type = "minecraft:uniform"; min_inclusive = @{
absolute = $MinY
}; max_inclusive = @{
absolute = $MaxY
}
}
$placement = @(@{
type = "minecraft:count"; count = $Count
}, @{
type = "minecraft:in_square"
}, @{
type = "minecraft:height_range"; height = $height
}, @{
type = "minecraft:biome"
})
Write-Json (Join-Path $dataRoot "fluxmachines/worldgen/placed_feature/ore_$Name.json") @{
feature = "fluxmachines:ore_$Name"; placement = $placement
}
Write-Json (Join-Path $dataRoot "fluxmachines/forge/biome_modifier/add_${Name}_ore.json") @{
type = "forge:add_features"; biomes = $Biomes; features = @("fluxmachines:ore_$Name"); step = "underground_ores"
}
}

$overworldTargets = @(
@{
target = @{
predicate_type = "minecraft:tag_match"; tag = "minecraft:stone_ore_replaceables"
}; state = @{
Name = "fluxmachines:titanium_ore"
}
},
@{
target = @{
predicate_type = "minecraft:tag_match"; tag = "minecraft:deepslate_ore_replaceables"
}; state = @{
Name = "fluxmachines:deepslate_titanium_ore"
}
}
)
Write-Json (Join-Path $dataRoot "fluxmachines/worldgen/configured_feature/ore_titanium.json") @{
type = "minecraft:ore"; config = @{
size = 5; discard_chance_on_air_exposure = 0.0; targets = $overworldTargets
}
}
Write-OreWorldgen "cobalt" 3 2 -64 -24 "minecraft:deepslate_ore_replaceables" "deepslate_cobalt_ore" "#minecraft:is_overworld"
Write-OreWorldgen "tungsten" 4 3 10 100 "minecraft:base_stone_nether" "tungsten_ore" "#minecraft:is_nether"
Write-OreWorldgen "osmium" 2 1 20 80 "minecraft:base_stone_nether" "osmium_ore" @("minecraft:basalt_deltas")
Write-OreWorldgen "iridium" 1 1 0 80 "minecraft:end_stone" "iridium_ore" @("minecraft:end_highlands", "minecraft:end_midlands")
$iridiumTarget = @{
target = @{
predicate_type = "minecraft:block_match"; block = "minecraft:end_stone"
}; state = @{
Name = "fluxmachines:iridium_ore"
}
}
Write-Json (Join-Path $dataRoot "fluxmachines/worldgen/configured_feature/ore_iridium.json") @{
type = "minecraft:ore"; config = @{
size = 1; discard_chance_on_air_exposure = 0.0; targets = @($iridiumTarget)
}
}
$titaniumHeight = @{
type = "minecraft:uniform"; min_inclusive = @{
absolute = -32
}; max_inclusive = @{
absolute = 48
}
}
Write-Json (Join-Path $dataRoot "fluxmachines/worldgen/placed_feature/ore_titanium.json") @{
feature = "fluxmachines:ore_titanium"; placement = @(@{
type = "minecraft:count"; count = 5
}, @{
type = "minecraft:in_square"
}, @{
type = "minecraft:height_range"; height = $titaniumHeight
}, @{
type = "minecraft:biome"
})
}
Write-Json (Join-Path $dataRoot "fluxmachines/forge/biome_modifier/add_titanium_ore.json") @{
type = "forge:add_features"; biomes = "#minecraft:is_overworld"; features = @("fluxmachines:ore_titanium"); step = "underground_ores"
}

function Item-In([string]$Name, [int]$Count = 1)
{
return @{
ingredient = @{
item = $Name
}; count = $Count
}
}
function Fluid-In([string]$Name, [int]$Amount)
{
return @{
fluid = $Name; amount = $Amount
}
}
function Item-Out([string]$Name, [int]$Count = 1)
{
return @{
item = $Name; count = $Count
}
}
function Write-Advanced([string]$Name, [string]$Machine, $Items, $Fluids, $ItemOutputs, $FluidOutputs, [int]$Duration, [int]$Energy)
{
$recipe = @{
type = "fluxmachines:advanced_processing"; machine = $Machine; item_inputs = @($Items); fluid_inputs = @($Fluids); item_outputs = @($ItemOutputs); fluid_outputs = @($FluidOutputs); duration = $Duration; energy_per_tick = $Energy
}
Write-Json (Join-Path $dataRoot "fluxmachines/recipes/$Name.json") $recipe
}

foreach ($metal in $metalColors.Keys)
{
Write-Advanced "resonant_crushing_$metal" "resonant_crusher" @((Item-In "fluxmachines:raw_$metal")) @() @((Item-Out "fluxmachines:crushed_$metal" 2)) @() 400 2048
Write-Advanced "leaching_$metal" "leaching_reactor" @((Item-In "fluxmachines:crushed_$metal" 8)) @((Fluid-In "fluxmachines:industrial_acid" 1000)) @() @((Fluid-In "fluxmachines:${metal}_slurry" 1000), (Fluid-In "fluxmachines:spent_acid" 250)) 1200 8192
Write-Advanced "purifying_$metal" "electrolytic_purifier" @((Item-In "minecraft:redstone" 4)) @((Fluid-In "fluxmachines:${metal}_slurry" 1000)) @((Item-Out "fluxmachines:purified_${metal}_dust" 6)) @((Fluid-In "fluxmachines:spent_acid" 500)) 1600 16384
Write-Advanced "plasma_sintering_$metal" "plasma_furnace" @((Item-In "fluxmachines:purified_${metal}_dust" 4)) @((Fluid-In "fluxmachines:cryogenic_coolant" 250)) @((Item-Out "fluxmachines:${metal}_ingot")) @() 2000 65536
}

Write-Advanced "resonant_crushing_sulfur" "resonant_crusher" @((Item-In "minecraft:blaze_powder")) @() @((Item-Out "fluxmachines:sulfur_dust" 2)) @() 300 2048
Write-Advanced "resonant_crushing_diamond" "resonant_crusher" @((Item-In "minecraft:diamond")) @() @((Item-Out "fluxmachines:diamond_dust")) @() 600 4096
Write-Advanced "industrial_acid" "leaching_reactor" @((Item-In "fluxmachines:sulfur_dust" 8), (Item-In "minecraft:gunpowder" 8)) @((Fluid-In "minecraft:water" 1000)) @() @((Fluid-In "fluxmachines:industrial_acid" 1000)) 1200 8192
Write-Advanced "acid_recycling" "leaching_reactor" @((Item-In "fluxmachines:sulfur_dust" 2)) @((Fluid-In "fluxmachines:spent_acid" 1000)) @() @((Fluid-In "fluxmachines:industrial_acid" 750)) 800 8192
Write-Advanced "cryogenic_coolant" "leaching_reactor" @((Item-In "minecraft:packed_ice" 8), (Item-In "minecraft:blue_ice" 2), (Item-In "fluxmachines:diamond_dust" 4)) @((Fluid-In "minecraft:water" 1000)) @() @((Fluid-In "fluxmachines:cryogenic_coolant" 1000)) 1600 16384
Write-Advanced "nutrient_gel" "leaching_reactor" @((Item-In "minecraft:golden_apple" 8), (Item-In "minecraft:golden_carrot" 8), (Item-In "minecraft:ghast_tear" 4)) @((Fluid-In "minecraft:water" 1000)) @() @((Fluid-In "fluxmachines:nutrient_gel" 1000)) 2000 16384

Write-Advanced "bio_inert_alloy" "quantum_assembler" @((Item-In "fluxmachines:titanium_ingot" 8), (Item-In "fluxmachines:cobalt_ingot" 8)) @((Fluid-In "fluxmachines:nutrient_gel" 1000)) @((Item-Out "fluxmachines:bio_inert_alloy" 4)) @() 2400 131072
Write-Advanced "tungsten_osmium_alloy" "quantum_assembler" @((Item-In "fluxmachines:tungsten_ingot" 8), (Item-In "fluxmachines:osmium_ingot" 8)) @((Fluid-In "fluxmachines:cryogenic_coolant" 1000)) @((Item-Out "fluxmachines:tungsten_osmium_alloy" 4)) @() 2400 131072
Write-Advanced "iridium_superconductor" "quantum_assembler" @((Item-In "fluxmachines:iridium_ingot" 4), (Item-In "fluxmachines:gold_wire" 8), (Item-In "fluxmachines:energy_lattice" 8)) @((Fluid-In "fluxmachines:cryogenic_coolant" 1000)) @((Item-Out "fluxmachines:iridium_superconductor" 2)) @() 3200 262144
Write-Advanced "diamond_capacitor_matrix" "quantum_assembler" @((Item-In "minecraft:diamond_block" 8), (Item-In "fluxmachines:high_capacity_cell" 2), (Item-In "fluxmachines:iridium_superconductor" 4)) @((Fluid-In "fluxmachines:cryogenic_coolant" 1000)) @((Item-Out "fluxmachines:diamond_capacitor_matrix")) @() 4800 262144
Write-Advanced "netherite_pressure_frame" "quantum_assembler" @((Item-In "minecraft:netherite_ingot" 8), (Item-In "fluxmachines:tungsten_osmium_alloy" 16)) @((Fluid-In "fluxmachines:cryogenic_coolant" 1000)) @((Item-Out "fluxmachines:netherite_pressure_frame")) @() 4800 262144
Write-Advanced "stellar_regulation_core" "quantum_assembler" @((Item-In "minecraft:nether_star" 4), (Item-In "fluxmachines:diamond_capacitor_matrix" 2), (Item-In "fluxmachines:netherite_pressure_frame")) @((Fluid-In "fluxmachines:nutrient_gel" 2000)) @((Item-Out "fluxmachines:stellar_regulation_core")) @() 6000 393216
Write-Advanced "first_aid_nanite_matrix" "quantum_assembler" @((Item-In "fluxmachines:stellar_regulation_core"), (Item-In "minecraft:totem_of_undying" 16), (Item-In "minecraft:ghast_tear" 32), (Item-In "fluxmachines:bio_inert_alloy" 64)) @((Fluid-In "fluxmachines:nutrient_gel" 4000)) @((Item-Out "fluxmachines:first_aid_nanite_matrix")) @() 8000 393216
Write-Advanced "nutrition_recycling_module" "quantum_assembler" @((Item-In "fluxmachines:stellar_regulation_core"), (Item-In "minecraft:golden_apple" 64), (Item-In "minecraft:golden_carrot" 64), (Item-In "fluxmachines:bio_inert_alloy" 32)) @((Fluid-In "fluxmachines:nutrient_gel" 4000)) @((Item-Out "fluxmachines:nutrition_recycling_module")) @() 8000 393216
Write-Advanced "force_field_matrix" "quantum_assembler" @((Item-In "fluxmachines:stellar_regulation_core"), (Item-In "fluxmachines:tungsten_osmium_alloy" 64), (Item-In "fluxmachines:iridium_superconductor" 32)) @((Fluid-In "fluxmachines:cryogenic_coolant" 4000)) @((Item-Out "fluxmachines:force_field_matrix")) @() 8000 393216
Write-Advanced "life_monitoring_core" "quantum_assembler" @((Item-In "fluxmachines:stellar_regulation_core"), (Item-In "minecraft:echo_shard" 32), (Item-In "minecraft:recovery_compass" 4), (Item-In "fluxmachines:energy_lattice" 32)) @((Fluid-In "fluxmachines:cryogenic_coolant" 4000)) @((Item-Out "fluxmachines:life_monitoring_core")) @() 8000 393216
Write-Advanced "iridium_ring_frame" "quantum_assembler" @((Item-In "fluxmachines:ring_frame"), (Item-In "fluxmachines:iridium_superconductor" 8), (Item-In "fluxmachines:bio_inert_alloy" 8)) @((Fluid-In "fluxmachines:cryogenic_coolant" 1000)) @((Item-Out "fluxmachines:iridium_ring_frame")) @() 6000 393216
Write-Advanced "electric_life_support_ring" "quantum_assembler" @((Item-In "fluxmachines:first_aid_nanite_matrix"), (Item-In "fluxmachines:nutrition_recycling_module"), (Item-In "fluxmachines:force_field_matrix"), (Item-In "fluxmachines:life_monitoring_core"), (Item-In "fluxmachines:iridium_ring_frame"), (Item-In "fluxmachines:induction_coil" 8)) @((Fluid-In "fluxmachines:nutrient_gel" 16000)) @((Item-Out "fluxmachines:electric_life_support_ring")) @() 24000 524288

function Write-Shaped([string]$Name, [string[]]$Pattern, $Key, [string]$Result)
{
Write-Json (Join-Path $dataRoot "fluxmachines/recipes/$Name.json") @{
type = "minecraft:crafting_shaped"; pattern = $Pattern; key = $Key; result = @{
item = $Result
}
}
}
Write-Shaped "corrosion_resistant_casing" @("RQR", "NIN", "RQR") @{
R = @{
item = "fluxmachines:reinforced_alloy"
}; Q = @{
item = "minecraft:quartz_block"
}; N = @{
item = "minecraft:netherite_ingot"
}; I = @{
item = "fluxmachines:machine_casing"
}
} "fluxmachines:corrosion_resistant_casing"
Write-Shaped "resonant_crusher" @("ACA", "CPC", "AHA") @{
A = @{
item = "fluxmachines:accelerator_core"
}; C = @{
item = "fluxmachines:corrosion_resistant_casing"
}; P = @{
item = "fluxmachines:pulverizer"
}; H = @{
item = "fluxmachines:high_capacity_cell"
}
} "fluxmachines:resonant_crusher"
Write-Shaped "leaching_reactor" @("CHC", "CAC", "CFC") @{
C = @{
item = "fluxmachines:corrosion_resistant_casing"
}; H = @{
item = "fluxmachines:high_capacity_cell"
}; A = @{
item = "fluxmachines:alloy_furnace"
}; F = @{
item = "minecraft:blast_furnace"
}
} "fluxmachines:leaching_reactor"
Write-Shaped "electrolytic_cell" @("DGD", "WEW", "DGD") @{
D = @{
item = "minecraft:diamond"
}; G = @{
item = "fluxmachines:gold_wire"
}; W = @{
item = "fluxmachines:copper_wire"
}; E = @{
item = "fluxmachines:energy_lattice"
}
} "fluxmachines:electrolytic_cell"
Write-Shaped "electrolytic_purifier" @("ECE", "CWC", "EHE") @{
E = @{
item = "fluxmachines:electrolytic_cell"
}; C = @{
item = "fluxmachines:corrosion_resistant_casing"
}; W = @{
item = "fluxmachines:wire_mill"
}; H = @{
item = "fluxmachines:high_capacity_cell"
}
} "fluxmachines:electrolytic_purifier"
Write-Shaped "plasma_coil" @("TCT", "OEO", "TCT") @{
T = @{
item = "fluxmachines:titanium_ingot"
}; C = @{
item = "fluxmachines:cobalt_ingot"
}; O = @{
item = "fluxmachines:induction_coil"
}; E = @{
item = "fluxmachines:energy_lattice"
}
} "fluxmachines:plasma_coil"
Write-Shaped "plasma_furnace" @("PCP", "CBC", "PHP") @{
P = @{
item = "fluxmachines:plasma_coil"
}; C = @{
item = "fluxmachines:corrosion_resistant_casing"
}; B = @{
item = "minecraft:blast_furnace"
}; H = @{
item = "fluxmachines:high_capacity_cell"
}
} "fluxmachines:plasma_furnace"
Write-Shaped "quantum_processor" @("ISI", "ECE", "ISI") @{
I = @{
item = "fluxmachines:iridium_superconductor"
}; S = @{
item = "minecraft:echo_shard"
}; E = @{
item = "fluxmachines:energy_lattice"
}; C = @{
item = "fluxmachines:accelerator_core"
}
} "fluxmachines:quantum_processor"
Write-Shaped "quantum_assembler" @("QHQ", "PAP", "QHQ") @{
Q = @{
item = "fluxmachines:quantum_processor"
}; H = @{
item = "fluxmachines:high_capacity_cell"
}; P = @{
item = "fluxmachines:plasma_coil"
}; A = @{
item = "fluxmachines:accelerator"
}
} "fluxmachines:quantum_assembler"

foreach ($metal in @("titanium", "cobalt"))
{
Write-Json (Join-Path $dataRoot "fluxmachines/recipes/${metal}_ingot_from_blasting.json") @{
type = "minecraft:blasting"; category = "misc"; cookingtime = 400; experience = 3.0; ingredient = @{
item = "fluxmachines:purified_${metal}_dust"
}; result = "fluxmachines:${metal}_ingot" }
}

Write-Host "Generated life-support industry resources in $resourceRoot"
