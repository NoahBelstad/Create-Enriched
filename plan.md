CREATE: ENRICHED - MASTER TECHNICAL SPECIFICATION
================================================================================
 * PROJECT OVERVIEW
   ================================================================================
   Create: Enriched is a 10-stage technical and narrative-driven add-on mod for Minecraft, built on NeoForge/Forge and integrated into the Create mod ecosystem.
The mod introduces advanced thermodynamics, nuclear physics, industrial chemistry, energy conversion, particle physics, quantum manipulation, and an endgame antimatter meta-narrative.
It features defensive code architecture, custom NBT data attachment capabilities, precise kinetic network interactions, custom multiblock validation, and strict error handling.
================================================================================
2. TECHNICAL PROGRESSION (STAGES 1 - 10)
STAGE 1: THERMODYNAMIC OVERHAUL & HIGH-PRESSURE STEAM
 * Introduced Fluid System: Custom SteamFluid replacing default steam behaviors.
 * Dynamic Pressure Formula: Pressure = Fluid Quantity (mB) / Total Network Volume (Blocks)
 * Failure Handlers: Over-pressurized pipes check structural limits every tick. Exceeding thresholds triggers explosive block destructions, drops broken pipe items, and creates thermal damage clouds.
 * Kinetic Mechanics: High-pressure steam feeds Steam Turbines and Mechanical Pistons, interfacing with Create's KineticBlockEntity to generate high Stress Capacity (SU).
STAGE 2: MODULAR THORIUM POWER
 * Multiblock Structure: Assembled using a 7x7 Mechanical Crafter setup to yield a 3-part reactor structure (Core, Boiler, Power Shaft).
 * Fuel Processing: Thorium ore processing into Low, Medium, and High Purity variants.
 * Mechanical Dampening: Graphite Control Rods inserted mechanically to reduce neutron multiplication factors.
 * Heat Management: Core heat decoupled from kinetic generation; heat must be routed through heat exchangers to drive Stage 1 steam networks.
STAGE 3: RADIATION PHYSIOLOGY & HAZARDS
 * Player Capability: IRadiationCapability attached to Player entities via NeoForge Attachments / Forge Capabilities.
 * NBT Persistence: Serializes total Sievert dosage (mSv) and decay rates across deaths and sessions.
 * Contamination Vectors: Radiation-emitting items, fluids, and world blocks project exposure within bounding box radiuses every N ticks.
 * Penetration Tiers:
   * Alpha: Shielded by basic inventory storage and leather/iron armor. Causes internal damage if ingested/inhaled.
   * Beta: Requires Copper-plated armor or HAZMAT suits to prevent surface burn damage and hunger debuffs.
   * Gamma: High penetration. Requires Lead-lined blocks, heavy concrete, or deep water buffers. Causes slowness, weakness, blindness, and lethal damage at high doses.
STAGE 4: INDUSTRIAL URANIUM LOGISTICS
 * World Generation & Prospecting: Rare Uranium deposits (1 per 3000x3000 block region) located via GeigerCompassItem.
 * High-Speed Fluid Drilling: 256 RPM minimum rotational speed supplied to FluidDrillBlockEntity, requiring acid coolant and 10-minute continuous extraction cycles.
 * Shielded Transit: Raw Uranium emits high Gamma radiation, requiring Lead-shielded train cars for safe transit across rail networks.
STAGE 5: CHEMICAL ISOTOPE ENRICHMENT
 * Refining Loop:
   * Ore Washing: Mechanical Belt washing to clean raw ore.
   * Acid Dissolution: Basin mixing with concentrated acid to produce Uranyl Nitrate.
   * Centrifuge Separation: Liquid processed in a kinetic CentrifugeBlockEntity within a strict speed range (192 RPM to 256 RPM) to isolate U-235 from U-238 slurry.
   * Pellet Compacting: Mechanical Pressing of U-235 in basin molds to form Fuel Pellets.
 * Waste Handling: Toxic spent waste must be stored in lead-lined tanks or vitrified into waste blocks.
STAGE 6: MODULAR MEGA-REACTORS
 * Core Construction: Built in-world using custom reactor casing, graphite moderators, and fuel channels.
 * Actuation: Rotational inputs move Control Rods via Gantry or Mechanical Bearing logic. Rod depth directly controls heat and SU output.
 * Automated Refueling: Spent fuel rods extracted via automated Mechanical Gantries with specialized extraction grippers during cooling cycles.
STAGE 7: STRESS-TO-ENERGY & LOGISTICS
 * Power Conversion: Transducer block converts rotational speed and torque (SU) directly to Forge Energy (FE).
   Formula: FE/t = Torque * RPM * Efficiency Factor
 * Matter Vault Multiblock: High-capacity storage managed by MatterCompressorBlockEntity.
   Compression Math: Volume Consumption(n) = Base Volume * e^(-k * n)
   Decreases storage footprints for duplicate items exponentially.
STAGE 8: RELATIVISTIC PARTICLE PHYSICS
 * Accelerator Structure: Large ring multiblock built from Superconducting Magnet blocks.
 * Geometric Alignment: Requires strict circular alignment. Path obstructions cause beam deflection, explosions, and block destruction.
 * Relativistic Synthesis: Consumes massive FE to accelerate particle beams to near light speed (0.99c), enabling synthesis of exotic isotopes and synthetic elements.
STAGE 9: REALITY CONTROL & AUTOMATION
 * Time Engine: Overrides local block tick rates (1x to 100x) or freezes entity ticks within a configurable area.
 * Molecular Replicator: Consumes high FE, liquid mass base, and item patterns to replicate non-exotic resources.
 * Quark Splitter: Direct mass-energy conversion unit (E=mc^2) turning FE streams directly into base materials.
 * Omni-Field Matrix Armor: Wireless power grid armor granting invulnerability, radiation immunity, vector flight, and instant block destruction.
 * Spatial Systems: Matter Teleporters, Gravity Manipulators, and Climate Controllers.
STAGE 10: ANTIMATTER & THE META-NARRATIVE
 * Antimatter Synthesis: Produced in Particle Accelerators via high-energy collisions.
 * Annihilation Reactor: Combines Matter and Antimatter inside a magnetic core to supply Infinite FE.
 * Spacetime Controller: Endgame multiblock requiring exotic isotopes, antimatter cores, and infinite energy networks.
 * Meta-Narrative Ending:
   * End Poem Event: Activating the controller triggers a dark screen transition with a cosmic entity dialogue reflecting on how complete automation removes survival friction and world engagement.
   * Post-Game World: Player returns to their world in a creative-equivalent state within an automated, silent world.
   * New Game+: Starting a new world triggers dialogue acknowledging the reset and emphasizing that constraints drive engagement.
================================================================================
3. EXECUTION SPRINTS
 * SPRINT 1: THERMODYNAMICS & RADIATION
   * Deliverables: Steam fluid tick math, pipe blowout logic, Thorium multiblock validation, persistent IRadiationCapability NBT data saving and syncing.
 * SPRINT 2: EXTRACTION & CENTRIFUGES
   * Deliverables: Uranium node world generation flags, 256 RPM Fluid Drill logic, Centrifuge block entity RPM calculations, Gantry refueling automation.
 * SPRINT 3: ENERGY, STORAGE & PHYSICS
   * Deliverables: SU-to-FE conversion tile entities, Matter Vault exponential storage math, Particle Accelerator beam rendering, Stage 9 reality control machines.
 * SPRINT 4: ANTIMATTER & META-NARRATIVE
   * Deliverables: Annihilation Reactor tile entity, Spacetime Controller UI and event handlers, custom End Poem UI, FTB Quests progression tree.
================================================================================
4. REPOSITORY & CODE STANDARDS
 * Branching Strategy: Main branch protected; development occurs on feature branches merged into dev.
 * Commit Style: Conventional Commits (feat, fix, refactor, docs).
 * Error Handling: Zero quiet catch blocks; explicit NBT validation with fallback default values; network packet verification on the server side to prevent invalid calls or client spoofing.
