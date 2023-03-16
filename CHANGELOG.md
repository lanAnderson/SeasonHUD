# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [1.4.8] - 2023-03-15

### Changed

- Re-enabled Xaero's Minimap Fairplay support
- Updated to support the latest version of Xaero's Minimap Fairplay


## [1.4.6] - 2023-02-20

### Changed

- Slight code cleanup
- Xaero's Minimap Fairplay still hasn't been updated to match the regular version, so continue using [1.4.4] if using it.


## [1.4.5] - 2023-02-12

### Fixed

- Fixed crash with Xaero's Minimap v23.1.0, due to the info display changes
- Temporarily disabled Fairplay functionality, until it is updated with those same changes


## [1.4.4] - 2023-02-04

### Added

- If the player is not in the overworld, the HUD will disable itself

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

## [1.4.0] - 2023-01-31

### Added

- Added optional support for CuriosAPI
    - If CuriosAPI is loaded, then the Calendar item from SereneSeason can be equipped in the "Charm" slot.
    - If the "Need Calendar" option is enabled, then a Calendar in the "Charm" slot will meet the requirement

## [1.3.5] - 2023-01-22

### Changed
- Synced version numbers back up with the main 1.19 branch
- Slight reorganization to match the 1.19 version's file structure

### Added
- Added config options to match the options available in 1.19

### Fixed
- Fixed config file not generating
- FTBChunks HUD not rendering
