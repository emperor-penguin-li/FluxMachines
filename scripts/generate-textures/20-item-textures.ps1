param(
    [string]$OutputRoot = (Join-Path $PSScriptRoot "../../build/generated/resources/main/assets/fluxmachines/textures")
)

. (Join-Path $PSScriptRoot "00-texture-library.ps1") -OutputRoot $OutputRoot
$itemDir = Join-Path $OutputRoot "item"

function New-DustTexture([System.Drawing.Color]$Dark, [System.Drawing.Color]$Base, [System.Drawing.Color]$Highlight) {
    $image = New-Texture $color.Clear
    Set-Points $image $color.Ink @(@(4,6),@(5,5),@(8,4),@(10,5),@(12,7),@(3,9),@(4,11),@(11,11),@(13,9))
    Set-Rectangle $image 4 7 9 4 $Dark
    Set-Rectangle $image 5 6 6 5 $Base
    Set-Points $image $Highlight @(@(6,6),@(9,5),@(5,8),@(8,7),@(11,8))
    Set-Points $image $Dark @(@(6,10),@(9,9),@(11,10),@(3,10))
    return $image
}

function New-WireTexture([System.Drawing.Color]$Dark, [System.Drawing.Color]$Base, [System.Drawing.Color]$Highlight) {
    $image = New-Texture $color.Clear
    Set-Rectangle $image 3 4 10 8 $color.Ink
    Set-Rectangle $image 4 5 8 6 $Dark
    Set-Rectangle $image 5 5 6 1 $Highlight
    Set-Rectangle $image 5 7 6 1 $Base
    Set-Rectangle $image 5 9 6 1 $Base
    Set-Rectangle $image 6 6 4 1 $color.Ink
    Set-Rectangle $image 6 8 4 1 $color.Ink
    Set-Points $image $Highlight @(@(4,6),@(11,6),@(4,8),@(11,8))
    return $image
}

$core = New-Texture $color.Clear
Set-Rectangle $core 6 1 4 1 $color.Shine
Set-Rectangle $core 4 2 8 1 $color.Light
Set-Rectangle $core 3 3 10 2 $color.Steel
Set-Rectangle $core 2 5 12 6 $color.Ink
Set-Rectangle $core 3 11 10 2 $color.Steel
Set-Rectangle $core 4 13 8 1 $color.Light
Set-Rectangle $core 6 14 4 1 $color.Shine
Set-Rectangle $core 4 4 8 8 $color.CyanDark
Set-Rectangle $core 5 3 6 10 $color.CyanDark
Set-Rectangle $core 5 5 6 6 $color.Cyan
Set-Rectangle $core 6 4 4 8 $color.Cyan
Set-Rectangle $core 7 5 2 6 $color.CyanLight
Set-Points $core $color.Amber @(@(2,7),@(13,7),@(2,8),@(13,8))
Save-Texture $core (Join-Path $itemDir "accelerator_core.png")

$connector = New-Texture $color.Clear
Set-Points $connector $color.Ink @(
    @(12,1),@(13,1),@(11,2),@(14,2),@(10,3),@(13,3),@(9,4),@(12,4),@(8,5),@(11,5),
    @(7,6),@(10,6),@(6,7),@(9,7),@(5,8),@(8,8),@(4,9),@(7,9),@(3,10),@(6,10),
    @(2,11),@(5,11),@(1,12),@(4,12),@(1,13),@(3,13),@(2,14)
)
Set-Points $connector $color.CyanLight @(@(12,2),@(13,2))
Set-Points $connector $color.Cyan @(@(11,3),@(12,3),@(10,4),@(11,4))
Set-Points $connector $color.Light @(@(9,5),@(10,5),@(8,6),@(9,6),@(7,7),@(8,7))
Set-Points $connector $color.Copper @(@(6,8),@(7,8),@(5,9),@(6,9),@(4,10),@(5,10),@(3,11),@(4,11),@(2,12),@(3,12))
Set-Points $connector $color.Amber @(@(7,7),@(8,6),@(5,10))
Set-Points $connector $color.CopperDark @(@(2,13),@(3,13))
Save-Texture $connector (Join-Path $itemDir "accelerator_connector.png")

$ring = New-Texture $color.Clear
Set-Points $ring $color.Steel @(@(5,3),@(6,2),@(7,2),@(8,2),@(9,2),@(10,3),@(11,4),@(12,5),@(12,6),@(12,9),@(11,10),@(10,11),@(9,12),@(8,13),@(7,13),@(6,13),@(5,12),@(4,11),@(3,10),@(3,9),@(3,6),@(4,5))
Set-Points $ring $color.Copper @(@(5,4),@(6,3),@(7,3),@(8,3),@(9,3),@(10,4),@(11,5),@(11,6),@(11,9),@(10,10),@(9,11),@(8,12),@(7,12),@(6,12),@(5,11),@(4,10),@(4,9),@(4,6),@(5,5))
Set-Rectangle $ring 6 6 5 4 $color.CyanDark
Set-Rectangle $ring 7 6 3 4 $color.Cyan
Set-Rectangle $ring 8 7 2 2 $color.Glow
Save-Texture $ring (Join-Path $itemDir "electric_flight_ring.png")

Save-Texture (New-DustTexture $color.Steel $color.Light $color.Shine) (Join-Path $itemDir "iron_dust.png")
Save-Texture (New-DustTexture $color.CopperDark $color.Copper $color.Amber) (Join-Path $itemDir "copper_dust.png")
Save-Texture (New-DustTexture $color.Copper $color.Amber $color.Glow) (Join-Path $itemDir "gold_dust.png")
Save-Texture (New-WireTexture $color.CopperDark $color.Copper $color.Amber) (Join-Path $itemDir "copper_wire.png")
Save-Texture (New-WireTexture $color.Copper $color.Amber $color.Glow) (Join-Path $itemDir "gold_wire.png")
Save-Texture (New-DustTexture $color.TinDark $color.Tin $color.Shine) (Join-Path $itemDir "tin_dust.png")
Save-Texture (New-WireTexture $color.TinDark $color.Tin $color.Shine) (Join-Path $itemDir "tin_wire.png")
Save-Texture (New-DustTexture $color.NickelDark $color.Nickel $color.Amber) (Join-Path $itemDir "nickel_dust.png")
Save-Texture (New-WireTexture $color.NickelDark $color.Nickel $color.Amber) (Join-Path $itemDir "nickel_wire.png")

function New-IngotTexture([System.Drawing.Color]$Dark, [System.Drawing.Color]$Base, [System.Drawing.Color]$Highlight) {
    $image = New-Texture $color.Clear
    Set-Points $image $color.Ink @(@(5,4),@(10,4),@(3,7),@(12,7),@(2,10),@(13,10),@(4,12),@(11,12))
    Set-Rectangle $image 4 5 8 2 $Highlight
    Set-Rectangle $image 3 7 10 4 $Base
    Set-Rectangle $image 4 11 8 2 $Dark
    Set-Rectangle $image 5 6 6 1 $color.Glow
    return $image
}

function New-RawMetalTexture([System.Drawing.Color]$Dark, [System.Drawing.Color]$Base, [System.Drawing.Color]$Highlight) {
    $image = New-Texture $color.Clear
    Set-Points $image $color.Ink @(@(5,3),@(9,3),@(3,5),@(11,5),@(2,8),@(12,8),@(4,12),@(10,12),@(7,13))
    Set-Rectangle $image 4 4 7 8 $Dark
    Set-Rectangle $image 3 6 9 5 $Base
    Set-Points $image $Highlight @(@(5,5),@(8,4),@(4,7),@(9,7),@(6,10))
    return $image
}

Save-Texture (New-IngotTexture $color.TinDark $color.Tin $color.Shine) (Join-Path $itemDir "tin_ingot.png")
Save-Texture (New-RawMetalTexture $color.TinDark $color.Tin $color.Shine) (Join-Path $itemDir "raw_tin.png")
Save-Texture (New-IngotTexture $color.NickelDark $color.Nickel $color.Amber) (Join-Path $itemDir "nickel_ingot.png")
Save-Texture (New-RawMetalTexture $color.NickelDark $color.Nickel $color.Amber) (Join-Path $itemDir "raw_nickel.png")

$redstoneAlloy = New-Texture $color.Clear
Set-Rectangle $redstoneAlloy 3 5 10 7 $color.Ink
Set-Rectangle $redstoneAlloy 4 4 8 7 $color.CopperDark
Set-Rectangle $redstoneAlloy 5 5 6 5 $color.Red
Set-Rectangle $redstoneAlloy 5 5 6 1 $color.Amber
Set-Points $redstoneAlloy $color.Glow @(@(6,6),@(9,8))
Save-Texture $redstoneAlloy (Join-Path $itemDir "redstone_alloy.png")

$fluxAlloy = New-Texture $color.Clear
Set-Rectangle $fluxAlloy 3 5 10 7 $color.Ink
Set-Rectangle $fluxAlloy 4 4 8 7 $color.CyanDark
Set-Rectangle $fluxAlloy 5 5 6 5 $color.Cyan
Set-Rectangle $fluxAlloy 5 5 6 1 $color.CyanLight
Set-Points $fluxAlloy $color.Glow @(@(6,6),@(9,8))
Save-Texture $fluxAlloy (Join-Path $itemDir "flux_alloy.png")

$reinforcedAlloy = New-IngotTexture $color.NickelDark $color.Steel $color.CyanLight
Set-Points $reinforcedAlloy $color.Cyan @(@(5,7),@(8,8),@(10,10))
Save-Texture $reinforcedAlloy (Join-Path $itemDir "reinforced_alloy.png")

$casing = New-Texture $color.Clear
Add-CasingFrame $casing $color.Steel $color.Tin $color.NickelDark
Set-Rectangle $casing 4 4 8 8 $color.Panel
Set-Rectangle $casing 6 6 4 4 $color.CyanDark
Set-Points $casing $color.Nickel @(@(3,3),@(12,3),@(3,12),@(12,12))
Save-Texture $casing (Join-Path $itemDir "machine_casing.png")

$coil = New-Texture $color.Clear
Set-Rectangle $coil 3 3 10 10 $color.Ink
Set-Rectangle $coil 4 4 8 8 $color.CopperDark
Set-Rectangle $coil 5 4 1 8 $color.Amber
Set-Rectangle $coil 7 4 1 8 $color.Copper
Set-Rectangle $coil 9 4 1 8 $color.Amber
Set-Rectangle $coil 11 4 1 8 $color.Copper
Set-Rectangle $coil 6 6 5 4 $color.Panel
Set-Rectangle $coil 7 7 3 2 $color.Cyan
Save-Texture $coil (Join-Path $itemDir "induction_coil.png")

$cell = New-Texture $color.Clear
Set-Rectangle $cell 4 2 8 13 $color.Ink
Set-Rectangle $cell 5 1 6 2 $color.Light
Set-Rectangle $cell 5 3 6 10 $color.CyanDark
Set-Rectangle $cell 6 4 4 8 $color.Cyan
Set-Rectangle $cell 7 5 2 5 $color.CyanLight
Set-Rectangle $cell 6 13 4 2 $color.Steel
Set-Points $cell $color.Glow @(@(7,4),@(8,4),@(8,10))
Save-Texture $cell (Join-Path $itemDir "high_capacity_cell.png")

$control = New-Texture $color.Clear
Set-Rectangle $control 3 3 10 10 $color.Ink
Set-Rectangle $control 4 4 8 8 $color.Steel
Set-Rectangle $control 5 5 6 6 $color.Panel
Set-Rectangle $control 6 6 4 4 $color.CyanDark
Set-Rectangle $control 7 7 2 2 $color.Glow
Set-Points $control $color.Amber @(@(2,5),@(2,8),@(13,5),@(13,8),@(5,2),@(8,2),@(5,13),@(8,13))
Save-Texture $control (Join-Path $itemDir "flight_control_core.png")

$matrix = New-Texture $color.Clear
Set-Points $matrix $color.Ink @(@(7,2),@(8,2),@(6,3),@(9,3),@(3,4),@(4,4),@(11,4),@(12,4),@(2,5),@(13,5),@(2,6),@(13,6),@(3,7),@(12,7),@(4,8),@(11,8),@(5,9),@(10,9),@(6,10),@(9,10),@(7,11),@(8,11))
Set-Points $matrix $color.Cyan @(@(7,3),@(8,3),@(5,4),@(6,4),@(9,4),@(10,4),@(3,5),@(4,5),@(11,5),@(12,5),@(4,6),@(5,6),@(10,6),@(11,6),@(5,7),@(6,7),@(9,7),@(10,7),@(6,8),@(9,8),@(7,9),@(8,9))
Set-Rectangle $matrix 7 5 2 3 $color.Glow
Save-Texture $matrix (Join-Path $itemDir "wing_matrix.png")

$frame = New-Texture $color.Clear
Set-Points $frame $color.Ink @(@(6,2),@(7,2),@(8,2),@(9,2),@(4,3),@(5,3),@(10,3),@(11,3),@(3,4),@(12,4),@(2,6),@(2,7),@(2,8),@(2,9),@(13,6),@(13,7),@(13,8),@(13,9),@(3,11),@(12,11),@(4,12),@(5,12),@(10,12),@(11,12),@(6,13),@(7,13),@(8,13),@(9,13))
Set-Points $frame $color.Amber @(@(6,3),@(7,3),@(8,3),@(9,3),@(4,4),@(5,4),@(10,4),@(11,4),@(3,6),@(3,7),@(3,8),@(3,9),@(12,6),@(12,7),@(12,8),@(12,9),@(4,10),@(5,11),@(10,11),@(11,10),@(6,12),@(7,12),@(8,12),@(9,12))
Set-Points $frame $color.Glow @(@(6,3),@(4,4),@(3,6),@(12,9),@(10,11))
Save-Texture $frame (Join-Path $itemDir "ring_frame.png")

$winding = New-Texture $color.Clear
Set-Rectangle $winding 3 3 10 10 $color.Ink
Set-Rectangle $winding 4 4 8 8 $color.Panel
Set-Rectangle $winding 5 4 1 8 $color.Copper
Set-Rectangle $winding 7 4 1 8 $color.Amber
Set-Rectangle $winding 9 4 1 8 $color.Copper
Set-Rectangle $winding 11 4 1 8 $color.Amber
Set-Rectangle $winding 5 6 7 1 $color.Red
Set-Rectangle $winding 5 9 7 1 $color.Red
Save-Texture $winding (Join-Path $itemDir "insulated_winding.png")

$lattice = New-Texture $color.Clear
Set-Points $lattice $color.Ink @(@(7,1),@(8,1),@(5,3),@(10,3),@(3,5),@(12,5),@(1,7),@(14,7),@(1,8),@(14,8),@(3,10),@(12,10),@(5,12),@(10,12),@(7,14),@(8,14))
Set-Points $lattice $color.Cyan @(@(7,2),@(8,2),@(5,4),@(10,4),@(3,6),@(12,6),@(2,7),@(13,7),@(2,8),@(13,8),@(3,9),@(12,9),@(5,11),@(10,11),@(7,13),@(8,13))
Set-Rectangle $lattice 5 5 6 6 $color.CyanDark
Set-Rectangle $lattice 6 6 4 4 $color.CyanLight
Set-Rectangle $lattice 7 7 2 2 $color.Glow
Save-Texture $lattice (Join-Path $itemDir "energy_lattice.png")

$stabilizer = New-Texture $color.Clear
Set-Rectangle $stabilizer 2 6 12 4 $color.Ink
Set-Rectangle $stabilizer 6 2 4 12 $color.Ink
Set-Rectangle $stabilizer 3 7 10 2 $color.Nickel
Set-Rectangle $stabilizer 7 3 2 10 $color.Steel
Set-Rectangle $stabilizer 5 5 6 6 $color.CyanDark
Set-Rectangle $stabilizer 6 6 4 4 $color.Cyan
Set-Rectangle $stabilizer 7 7 2 2 $color.Glow
Save-Texture $stabilizer (Join-Path $itemDir "inertial_stabilizer.png")

$emitter = New-Texture $color.Clear
Set-Points $emitter $color.Ink @(@(7,2),@(8,2),@(5,3),@(10,3),@(4,5),@(11,5),@(3,7),@(12,7),@(4,10),@(11,10),@(6,12),@(9,12),@(7,13),@(8,13))
Set-Points $emitter $color.Steel @(@(7,3),@(8,3),@(5,4),@(10,4),@(4,6),@(11,6),@(4,8),@(11,8),@(5,10),@(10,10),@(7,12),@(8,12))
Set-Rectangle $emitter 6 5 4 6 $color.CyanDark
Set-Rectangle $emitter 7 6 2 4 $color.CyanLight
Set-Points $emitter $color.Glow @(@(7,4),@(8,4),@(7,10),@(8,10))
Save-Texture $emitter (Join-Path $itemDir "lift_emitter.png")

$band = New-Texture $color.Clear
Set-Points $band $color.Ink @(@(6,2),@(7,2),@(8,2),@(9,2),@(4,3),@(5,3),@(10,3),@(11,3),@(3,5),@(12,5),@(2,7),@(13,7),@(3,10),@(12,10),@(5,12),@(10,12),@(7,13),@(8,13))
Set-Points $band $color.Amber @(@(6,3),@(7,3),@(8,3),@(9,3),@(4,4),@(5,4),@(10,4),@(11,4),@(3,6),@(12,6),@(3,8),@(12,8),@(4,10),@(11,10),@(5,11),@(10,11),@(7,12),@(8,12))
Set-Points $band $color.Cyan @(@(5,4),@(10,4),@(3,8),@(12,8),@(5,11),@(10,11))
Save-Texture $band (Join-Path $itemDir "conductive_band.png")

Write-Host "Generated item textures in $itemDir"
