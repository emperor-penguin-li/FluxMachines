param(
    [string]$OutputRoot = (Join-Path $PSScriptRoot "../../build/generated/resources/main/assets/fluxmachines/textures")
)

. (Join-Path $PSScriptRoot "00-texture-library.ps1") -OutputRoot $OutputRoot
$blockDir = Join-Path $OutputRoot "block"

# Accelerator: reinforced cyan flux chamber.
$side = New-Texture $color.Ink
Add-CasingFrame $side $color.Steel $color.Light $color.Shadow
Set-Rectangle $side 3 3 10 10 $color.Panel
Set-Rectangle $side 5 4 6 8 $color.CyanDark
Set-Rectangle $side 6 4 4 8 $color.Cyan
Set-Rectangle $side 7 5 2 6 $color.CyanLight
Set-Points $side $color.Copper @(@(3,7),@(12,7),@(3,8),@(12,8))
Save-Texture $side (Join-Path $blockDir "accelerator_side.png")

$top = New-Texture $color.Ink
Add-CasingFrame $top $color.Steel $color.Light $color.Shadow
Set-Rectangle $top 3 3 10 10 $color.Panel
Set-Rectangle $top 4 4 8 8 $color.CyanDark
Set-Rectangle $top 5 5 6 6 $color.Cyan
Set-Rectangle $top 6 6 4 4 $color.CyanLight
Set-Rectangle $top 7 7 2 2 $color.Glow
Set-Points $top $color.Copper @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $top (Join-Path $blockDir "accelerator_top.png")

$bottom = New-Texture $color.Ink
Add-CasingFrame $bottom $color.Steel $color.Light $color.Shadow
Set-Rectangle $bottom 4 4 8 8 $color.Ink
Set-Rectangle $bottom 5 5 6 6 $color.Panel
Set-Rectangle $bottom 7 5 2 6 $color.CyanDark
Set-Points $bottom $color.Copper @(@(2,2),@(3,2),@(2,3),@(13,2),@(12,2),@(13,3),@(2,12),@(2,13),@(3,13),@(12,13),@(13,13),@(13,12))
Save-Texture $bottom (Join-Path $blockDir "accelerator_bottom.png")

# Pulverizer: heavy blue-steel housing with interlocking crusher teeth.
$side = New-Texture $color.Ink
Add-CasingFrame $side $color.Steel $color.Light $color.Shadow
Set-Rectangle $side 3 3 10 10 $color.Graphite
Set-Rectangle $side 4 5 8 6 $color.Ink
Set-Points $side $color.Shine @(@(4,5),@(6,5),@(8,5),@(10,5),@(5,10),@(7,10),@(9,10),@(11,10))
Set-Points $side $color.Light @(@(5,6),@(7,6),@(9,6),@(11,6),@(4,9),@(6,9),@(8,9),@(10,9))
Set-Rectangle $side 7 7 2 2 $color.Cyan
Save-Texture $side (Join-Path $blockDir "pulverizer_side.png")

$top = New-Texture $color.Ink
Add-CasingFrame $top $color.Steel $color.Light $color.Shadow
Set-Rectangle $top 3 3 10 10 $color.Graphite
Set-Rectangle $top 5 3 2 10 $color.Ink
Set-Rectangle $top 9 3 2 10 $color.Ink
Set-Rectangle $top 6 5 4 2 $color.Shine
Set-Rectangle $top 6 9 4 2 $color.Light
Set-Points $top $color.Cyan @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $top (Join-Path $blockDir "pulverizer_top.png")

$bottom = New-Texture $color.Ink
Add-CasingFrame $bottom $color.Shadow $color.Steel $color.Ink
Set-Rectangle $bottom 4 4 8 8 $color.Graphite
Set-Rectangle $bottom 6 6 4 4 $color.Ink
Set-Points $bottom $color.Light @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $bottom (Join-Path $blockDir "pulverizer_bottom.png")

# Wire mill: brass drive housing, paired copper rollers and a drawn cyan wire.
$side = New-Texture $color.Ink
Add-CasingFrame $side $color.CopperDark $color.Copper $color.Shadow
Set-Rectangle $side 3 3 10 10 $color.Panel
Set-Rectangle $side 4 4 3 8 $color.Copper
Set-Rectangle $side 9 4 3 8 $color.Copper
Set-Rectangle $side 5 5 1 6 $color.Amber
Set-Rectangle $side 10 5 1 6 $color.Amber
Set-Rectangle $side 7 7 2 2 $color.CyanLight
Set-Rectangle $side 7 9 2 4 $color.Cyan
Save-Texture $side (Join-Path $blockDir "wire_mill_side.png")

$top = New-Texture $color.Ink
Add-CasingFrame $top $color.CopperDark $color.Copper $color.Shadow
Set-Rectangle $top 3 3 10 10 $color.Panel
Set-Rectangle $top 4 5 8 2 $color.Copper
Set-Rectangle $top 4 9 8 2 $color.Copper
Set-Rectangle $top 5 6 1 4 $color.Amber
Set-Rectangle $top 10 6 1 4 $color.Amber
Set-Rectangle $top 7 3 2 10 $color.Cyan
Set-Points $top $color.Glow @(@(7,4),@(8,8))
Save-Texture $top (Join-Path $blockDir "wire_mill_top.png")

$bottom = New-Texture $color.Ink
Add-CasingFrame $bottom $color.CopperDark $color.Copper $color.Shadow
Set-Rectangle $bottom 4 4 8 8 $color.Graphite
Set-Rectangle $bottom 5 7 6 2 $color.Copper
Set-Points $bottom $color.Amber @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $bottom (Join-Path $blockDir "wire_mill_bottom.png")

# Alloy furnace: dark refractory casing surrounding a hot crucible.
$side = New-Texture $color.Ink
Add-CasingFrame $side $color.Graphite $color.Light $color.Ink
Set-Rectangle $side 3 3 10 10 $color.Shadow
Set-Rectangle $side 4 5 8 7 $color.Ink
Set-Rectangle $side 5 7 6 4 $color.RedDark
Set-Rectangle $side 6 8 4 3 $color.Red
Set-Rectangle $side 7 9 2 2 $color.Amber
Set-Rectangle $side 4 4 8 1 $color.Steel
Set-Points $side $color.Red @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $side (Join-Path $blockDir "alloy_furnace_side.png")

$top = New-Texture $color.Ink
Add-CasingFrame $top $color.Graphite $color.Light $color.Ink
Set-Rectangle $top 3 3 10 10 $color.Shadow
Set-Rectangle $top 4 4 8 8 $color.Ink
Set-Rectangle $top 5 5 6 6 $color.RedDark
Set-Rectangle $top 6 6 4 4 $color.Red
Set-Rectangle $top 7 7 2 2 $color.Amber
Set-Points $top $color.Steel @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $top (Join-Path $blockDir "alloy_furnace_top.png")

$bottom = New-Texture $color.Ink
Add-CasingFrame $bottom $color.Graphite $color.Steel $color.Ink
Set-Rectangle $bottom 3 3 10 10 $color.Shadow
Set-Rectangle $bottom 5 5 6 6 $color.Ink
Set-Points $bottom $color.RedDark @(@(4,4),@(11,4),@(4,11),@(11,11),@(7,7),@(8,8))
Save-Texture $bottom (Join-Path $blockDir "alloy_furnace_bottom.png")

# Charging station: mid-tier conductive pad with a blue flux glyph.
$side = New-Texture $color.Ink
Add-CasingFrame $side $color.Graphite $color.Light $color.Shadow
Set-Rectangle $side 3 4 10 8 $color.Panel
Set-Rectangle $side 4 5 8 1 $color.Steel
Set-Rectangle $side 4 10 8 1 $color.Steel
Set-Rectangle $side 6 6 4 4 $color.CyanDark
Set-Rectangle $side 7 7 2 2 $color.Cyan
Set-Points $side $color.Copper @(@(3, 3), @(12, 3), @(3, 12), @(12, 12))
Save-Texture $side (Join-Path $blockDir "charging_station_side.png")

$top = New-Texture $color.Ink
Add-CasingFrame $top $color.Graphite $color.Light $color.Shadow
Set-Rectangle $top 3 3 10 10 $color.Panel
Set-Rectangle $top 4 4 8 8 $color.Graphite
Set-Rectangle $top 5 5 6 6 $color.CyanDark
Set-Rectangle $top 7 4 2 8 $color.Steel
Set-Rectangle $top 4 7 8 2 $color.Steel
Set-Points $top $color.Cyan @(@(5, 5), @(10, 5), @(5, 10), @(10, 10))
$topActive = $top.Clone()
Save-Texture $top (Join-Path $blockDir "charging_station_top.png")

Set-Rectangle $topActive 5 5 6 6 $color.Cyan
Set-Rectangle $topActive 7 4 2 8 $color.CyanLight
Set-Rectangle $topActive 4 7 8 2 $color.CyanLight
Set-Rectangle $topActive 7 7 2 2 $color.Glow
Save-Texture $topActive (Join-Path $blockDir "charging_station_top_active.png")

$bottom = New-Texture $color.Ink
Add-CasingFrame $bottom $color.Graphite $color.Steel $color.Shadow
Set-Rectangle $bottom 4 4 8 8 $color.Panel
Set-Rectangle $bottom 6 6 4 4 $color.Ink
Set-Points $bottom $color.Copper @(@(3, 3), @(12, 3), @(3, 12), @(12, 12))
Save-Texture $bottom (Join-Path $blockDir "charging_station_bottom.png")

function New-OreTexture([System.Drawing.Color]$Stone, [System.Drawing.Color]$StoneLight, [System.Drawing.Color]$OreDark, [System.Drawing.Color]$Ore, [System.Drawing.Color]$OreLight) {
    $image = New-Texture $Stone
    Set-Rectangle $image 0 0 16 2 $StoneLight
    Set-Rectangle $image 1 5 6 4 $StoneLight
    Set-Rectangle $image 9 10 7 4 $StoneLight
    Set-Points $image $color.Shadow @(@(2,3),@(7,2),@(12,4),@(4,11),@(8,8),@(14,7),@(1,14),@(10,15))
    Set-Points $image $OreDark @(@(3,4),@(4,4),@(10,3),@(11,3),@(6,8),@(7,8),@(12,11),@(13,11),@(3,13))
    Set-Points $image $Ore @(@(4,5),@(5,5),@(10,4),@(11,4),@(7,9),@(8,9),@(12,12),@(13,12),@(4,13),@(5,13))
    Set-Points $image $OreLight @(@(5,4),@(11,3),@(8,8),@(13,11),@(5,12))
    return $image
}

Save-Texture (New-OreTexture $color.Steel $color.Light $color.TinDark $color.Tin $color.Shine) (Join-Path $blockDir "tin_ore.png")
Save-Texture (New-OreTexture $color.Graphite $color.Steel $color.TinDark $color.Tin $color.Shine) (Join-Path $blockDir "deepslate_tin_ore.png")
Save-Texture (New-OreTexture $color.Steel $color.Light $color.NickelDark $color.Nickel $color.Amber) (Join-Path $blockDir "nickel_ore.png")
Save-Texture (New-OreTexture $color.Graphite $color.Steel $color.NickelDark $color.Nickel $color.Amber) (Join-Path $blockDir "deepslate_nickel_ore.png")

# Mob suppressor: modular graphite tower with cyan field channels.
$side = New-Texture $color.Ink
Add-CasingFrame $side $color.Graphite $color.Light $color.Shadow
Set-Rectangle $side 3 3 10 10 $color.Panel
Set-Rectangle $side 6 3 4 10 $color.CyanDark
Set-Rectangle $side 7 4 2 8 $color.Cyan
Set-Points $side $color.Copper @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $side (Join-Path $blockDir "mob_suppressor_side.png")

$top = New-Texture $color.Ink
Add-CasingFrame $top $color.Graphite $color.Light $color.Shadow
Set-Rectangle $top 3 3 10 10 $color.Panel
Set-Rectangle $top 5 5 6 6 $color.CyanDark
Set-Rectangle $top 7 3 2 10 $color.Cyan
Set-Rectangle $top 3 7 10 2 $color.Cyan
Save-Texture $top (Join-Path $blockDir "mob_suppressor_top.png")

$front = New-Texture $color.Ink
Add-CasingFrame $front $color.Graphite $color.Light $color.Shadow
Set-Rectangle $front 3 3 10 10 $color.Panel
Set-Rectangle $front 5 5 6 6 $color.CyanDark
Set-Rectangle $front 7 7 2 2 $color.Steel
$frontActive = $front.Clone()
Save-Texture $front (Join-Path $blockDir "mob_suppressor_front.png")
Set-Rectangle $frontActive 5 5 6 6 $color.Cyan
Set-Rectangle $frontActive 6 6 4 4 $color.CyanLight
Set-Rectangle $frontActive 7 7 2 2 $color.Glow
Save-Texture $frontActive (Join-Path $blockDir "mob_suppressor_front_active.png")

$casing = New-Texture $color.Ink
Add-CasingFrame $casing $color.Steel $color.Light $color.Shadow
Set-Rectangle $casing 3 3 10 10 $color.Graphite
Set-Points $casing $color.Copper @(@(4,4),@(11,4),@(4,11),@(11,11))
Set-Rectangle $casing 7 3 2 10 $color.CyanDark
Save-Texture $casing (Join-Path $blockDir "suppressor_casing.png")

$coil = New-Texture $color.Panel
Add-CasingFrame $coil $color.CopperDark $color.Copper $color.Shadow
Set-Rectangle $coil 3 3 10 10 $color.CyanDark
Set-Rectangle $coil 4 4 8 8 $color.Ink
Set-Rectangle $coil 5 5 6 6 $color.Cyan
Set-Rectangle $coil 7 3 2 10 $color.CyanLight
Set-Points $coil $color.Glow @(@(6,6),@(9,6),@(6,9),@(9,9))
Save-Texture $coil (Join-Path $blockDir "suppressor_coil.png")

$emitter = New-Texture $color.Ink
Add-CasingFrame $emitter $color.Graphite $color.Light $color.Shadow
Set-Rectangle $emitter 3 3 10 10 $color.CyanDark
Set-Rectangle $emitter 5 5 6 6 $color.Cyan
Set-Rectangle $emitter 7 2 2 12 $color.CyanLight
Set-Rectangle $emitter 2 7 12 2 $color.CyanLight
Set-Rectangle $emitter 7 7 2 2 $color.Glow
Save-Texture $emitter (Join-Path $blockDir "suppressor_emitter.png")

Write-Host "Generated machine textures in $blockDir"
