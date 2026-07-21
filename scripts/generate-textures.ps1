param(
    [string]$OutputRoot = (Join-Path $PSScriptRoot "../build/generated/resources/main/assets/fluxmachines/textures")
)

Add-Type -AssemblyName System.Drawing

$blockDir = Join-Path $OutputRoot "block"
$itemDir = Join-Path $OutputRoot "item"
New-Item -ItemType Directory -Force -Path $blockDir, $itemDir | Out-Null

$color = @{
    Clear = [System.Drawing.Color]::Transparent
    Ink = [System.Drawing.Color]::FromArgb(255, 24, 30, 37)
    Shadow = [System.Drawing.Color]::FromArgb(255, 39, 48, 57)
    Steel = [System.Drawing.Color]::FromArgb(255, 72, 86, 98)
    Light = [System.Drawing.Color]::FromArgb(255, 119, 137, 148)
    Shine = [System.Drawing.Color]::FromArgb(255, 173, 190, 196)
    Panel = [System.Drawing.Color]::FromArgb(255, 18, 39, 45)
    CyanDark = [System.Drawing.Color]::FromArgb(255, 0, 98, 112)
    Cyan = [System.Drawing.Color]::FromArgb(255, 0, 209, 221)
    CyanLight = [System.Drawing.Color]::FromArgb(255, 151, 255, 246)
    Glow = [System.Drawing.Color]::FromArgb(255, 225, 255, 252)
    CopperDark = [System.Drawing.Color]::FromArgb(255, 105, 51, 29)
    Copper = [System.Drawing.Color]::FromArgb(255, 205, 103, 45)
    Amber = [System.Drawing.Color]::FromArgb(255, 255, 190, 61)
}

function New-Texture([System.Drawing.Color]$Background) {
    $image = [System.Drawing.Bitmap]::new(16, 16, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    Set-Rectangle $image 0 0 16 16 $Background
    return $image
}

function Set-Rectangle($Image, [int]$X, [int]$Y, [int]$Width, [int]$Height, [System.Drawing.Color]$Color) {
    for ($py = $Y; $py -lt $Y + $Height; $py++) {
        for ($px = $X; $px -lt $X + $Width; $px++) {
            $Image.SetPixel($px, $py, $Color)
        }
    }
}

function Set-Points($Image, [System.Drawing.Color]$Color, [object[]]$Points) {
    foreach ($point in $Points) {
        $Image.SetPixel([int]$point[0], [int]$point[1], $Color)
    }
}

function Save-Texture($Image, [string]$Path) {
    $Image.Save($Path, [System.Drawing.Imaging.ImageFormat]::Png)
    $Image.Dispose()
}

# Reinforced casing around a luminous flux chamber.
$side = New-Texture $color.Ink
Set-Rectangle $side 1 1 14 14 $color.Steel
Set-Rectangle $side 3 3 10 10 $color.Panel
Set-Rectangle $side 5 4 6 8 $color.CyanDark
Set-Rectangle $side 6 4 4 8 $color.Cyan
Set-Rectangle $side 7 5 2 6 $color.CyanLight
Set-Rectangle $side 1 1 14 1 $color.Light
Set-Rectangle $side 1 2 1 13 $color.Light
Set-Rectangle $side 2 14 13 1 $color.Shadow
Set-Rectangle $side 14 2 1 13 $color.Shadow
Set-Points $side $color.Copper @(@(3, 7), @(12, 7), @(3, 8), @(12, 8))
Save-Texture $side (Join-Path $blockDir "accelerator_side.png")

# Concentric top frame and energized aperture.
$top = New-Texture $color.Ink
Set-Rectangle $top 1 1 14 14 $color.Steel
Set-Rectangle $top 1 1 14 1 $color.Light
Set-Rectangle $top 1 2 1 13 $color.Light
Set-Rectangle $top 2 14 13 1 $color.Shadow
Set-Rectangle $top 14 2 1 13 $color.Shadow
Set-Rectangle $top 3 3 10 10 $color.Panel
Set-Rectangle $top 4 4 8 8 $color.CyanDark
Set-Rectangle $top 5 5 6 6 $color.Cyan
Set-Rectangle $top 6 6 4 4 $color.CyanLight
Set-Rectangle $top 7 7 2 2 $color.Glow
Set-Points $top $color.Copper @(@(3, 3), @(12, 3), @(3, 12), @(12, 12))
Save-Texture $top (Join-Path $blockDir "accelerator_top.png")

# Sealed mounting plate with four copper contacts.
$bottom = New-Texture $color.Ink
Set-Rectangle $bottom 1 1 14 14 $color.Steel
Set-Rectangle $bottom 1 1 14 1 $color.Light
Set-Rectangle $bottom 1 2 1 13 $color.Light
Set-Rectangle $bottom 2 14 13 1 $color.Shadow
Set-Rectangle $bottom 14 2 1 13 $color.Shadow
Set-Rectangle $bottom 4 4 8 8 $color.Ink
Set-Rectangle $bottom 5 5 6 6 $color.Panel
Set-Rectangle $bottom 7 5 2 6 $color.CyanDark
Set-Rectangle $bottom 2 2 2 2 $color.Copper
Set-Rectangle $bottom 12 2 2 2 $color.Copper
Set-Rectangle $bottom 2 12 2 2 $color.Copper
Set-Rectangle $bottom 12 12 2 2 $color.Copper
Save-Texture $bottom (Join-Path $blockDir "accelerator_bottom.png")

# Compact crystalline flux cell held by a steel ring.
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
Set-Points $core $color.Shine @(@(3, 4), @(12, 4), @(3, 11), @(12, 11))
Set-Points $core $color.Amber @(@(2, 7), @(13, 7), @(2, 8), @(13, 8))
Save-Texture $core (Join-Path $itemDir "accelerator_core.png")

# Handheld copper probe with a cyan locator crystal.
$connector = New-Texture $color.Clear
Set-Points $connector $color.Ink @(
    @(12, 1), @(13, 1), @(11, 2), @(14, 2), @(10, 3), @(13, 3), @(9, 4), @(12, 4),
    @(8, 5), @(11, 5), @(7, 6), @(10, 6), @(6, 7), @(9, 7), @(5, 8), @(8, 8),
    @(4, 9), @(7, 9), @(3, 10), @(6, 10), @(2, 11), @(5, 11), @(1, 12), @(4, 12),
    @(1, 13), @(3, 13), @(2, 14)
)
Set-Points $connector $color.CyanLight @(@(12, 2), @(13, 2))
Set-Points $connector $color.Cyan @(@(11, 3), @(12, 3), @(10, 4), @(11, 4))
Set-Points $connector $color.Light @(@(9, 5), @(10, 5), @(8, 6), @(9, 6), @(7, 7), @(8, 7))
Set-Points $connector $color.Copper @(
    @(6, 8), @(7, 8), @(5, 9), @(6, 9), @(4, 10), @(5, 10), @(3, 11), @(4, 11),
    @(2, 12), @(3, 12)
)
Set-Points $connector $color.Amber @(@(7, 7), @(8, 6), @(5, 10))
Set-Points $connector $color.CopperDark @(@(2, 13), @(3, 13))
Save-Texture $connector (Join-Path $itemDir "accelerator_connector.png")

Write-Host "Generated FluxMachines textures in $OutputRoot"
