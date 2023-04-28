# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [1.4.14] - 2023-04-27

### Fixed

- Overlapping with Open Parties and Claims


## [1.4.13] - 2023-04-26

### Fixed

- Fixed the HUD staying rendered while inventories are open, while using Journeymap or no minimap


## [1.4.12] - 2023-04-22

### Fixed

- Fixed background color of Journeymap labels if the theme is changed


## [1.4.11] - 2023-04-18

### Fixed

- The season HUD not showing up while using FTBChunks on a server


## [1.4.10] - 2023-04-05

### Fixed

- Fixed alignment issue with JourneyMap on the bottom half the screen

### Added

- Added config option to display above the JourneyMap minimap


## [1.4.9] - 2023-04-02

### Changed

- Slight code cleanup


## [1.4.8] - 2023-03-15

### Changed

- Re-enabled Xaero's Minimap Fairplay support
- Updated to support the latest version of Xaero's Minimap Fairplay


## [1.4.7] - 2023-03-02

### Changed

- Updated to the release version of Serene Seasons

# [1.4.6] - 2023-02-20

### Changed

- Slight code cleanup
- Xaero's Minimap Fairplay still hasn't been updated to match the regular version, so continue using [1.4.4] if using it.


## [1.4.5] - 2023-02-12

### Fixed

- Fixed crash with Xaero's Minimap v23.1.0, due to the info display changes
- Temporarily disabled Fairplay functionality, until it is updated with those same changes


## [1.4.4] - 2023-02-04

### Added

- If the player is not in the overworld, the HUD will disable itself.

### Changed

- Some slight code cleanup.


## [1.4.3] - 2023-02-03

### Added

- Added a config option to show normal seasons in tropical biomes


## [1.4.2] - 2023-02-02

### Fixed

- Attempt #2 of fixing UI overlap in a claimed chunk


## [1.4.1] - 2023-02-02

### Fixed

- Fixed UI overlap if in a claimed chunk while using FTB Chunks


## [1.4.0] - 2023-01-30

### Added

- Added optional support for CuriosAPI
  - If CuriosAPI is loaded, then the Calendar item from SereneSeason can be equipped in the "Charm" slot.
  - If the "Need Calendar" option is enabled, then a Calendar in the "Charm" slot will meet the requirement

## [1.3.6] - 2023-01-21

### Changed
- Some code cleanup

## [1.3.5] - 2023-01-20

### Added

- Added support for Xaero's Minimap Fair-play

## [1.3.4] - 2023-01-19

### Added

- Added an option to show the HUD only if the player has the Calendar item in their inventory

## [1.3.3] - 2023-01-11

### Added

- Added config option with presets for HUD location (when no minimap is present)
- Added button in config screen for HUD location

## [1.3.2] - 2023-01-10

### Added

- Added config options to offset the default HUD

### Changed

- Built against latest recommended Forge version

## [1.3.1] - 2023-01-06

### Added

- Added an option to disable the mod ingame.


### Changed

- Slight code cleanup

## [1.3.0] - 2022-12-29

### Added

- Added am in-game config screen and a keybind for it.
  - Default key is 'H'


### Changed

- Due to some changes from 1.19.2 -> 1.19.3, I had to change to separate builds for each, rather than a unified version
- Fixed an issue with tropical season dates. Should be accurate now.