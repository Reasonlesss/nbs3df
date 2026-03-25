#!/usr/bin/env python3

# this file was vibecoded by copilot

import json
import re
from pathlib import Path

def load_json(filename):
    """Load a JSON file from the datagen directory."""
    with open(filename, 'r') as f:
        return json.load(f)

def strip_color_codes(text):
    """Remove Minecraft color codes (§x) from text."""
    return re.sub(r'§.', '', text)

def convert_to_friendly_name(sound_key):
    """Convert a sound key like 'ambient.basalt_deltas.additions' to 'Ambient Basalt Deltas Additions'."""
    return ' '.join(word.capitalize() for word in sound_key.replace('.', ' ').replace('_', ' ').split())

def main():
    print("Loading data files...")
    sounds = load_json('sounds.json')
    sound_seeds = load_json('sound_seeds.json')
    actiondump = load_json('actiondump.json')
    
    print(f"Loaded {len(sounds)} sound entries")
    print(f"Loaded {len(sound_seeds)} sound seed entries")
    
    # Build a mapping from soundId to friendly name from actiondump
    actiondump_sounds = actiondump.get('sounds', [])
    print(f"Loaded {len(actiondump_sounds)} actiondump sound entries")
    
    sound_id_to_friendly = {}
    sound_id_to_variants = {}
    
    for sound_entry in actiondump_sounds:
        sound_id = sound_entry.get('soundId')
        icon = sound_entry.get('icon', {})
        name = icon.get('name', '')
        if sound_id and name:
            # Strip Minecraft color codes
            friendly_name = strip_color_codes(name)
            sound_id_to_friendly[sound_id] = friendly_name
        
        # Build variant mapping (id -> name)
        if sound_id:
            variants = sound_entry.get('variants', [])
            variant_map = {}
            for variant in variants:
                variant_id = variant.get('id')
                variant_name = variant.get('name')
                if variant_id and variant_name:
                    variant_map[variant_id] = variant_name
            if variant_map:
                sound_id_to_variants[sound_id] = variant_map
    
    print(f"Built {len(sound_id_to_friendly)} friendly name mappings from actiondump")
    print(f"Built {len(sound_id_to_variants)} variant mappings from actiondump")
    
    # Generate friendly_names.json
    # Maps friendly name -> minecraft sound key
    friendly_names = {}
    missing_count = 0
    
    for sound_key in sounds.keys():
        minecraft_key = f"minecraft:{sound_key}"
        # Get friendly name from actiondump, null if not found
        friendly_name = sound_id_to_friendly.get(sound_key)
        if not friendly_name:
            friendly_name = None
            missing_count += 1
        friendly_names[friendly_name if friendly_name else sound_key] = minecraft_key
    
    if missing_count > 0:
        print(f"⚠ {missing_count} sounds not in actiondump - using null for friendly name")
    
    print(f"\nGenerated {len(friendly_names)} friendly name mappings")
    
    # Generate sound_files.json
    # Maps sound file name -> {key, seed, name, seed_name}
    sound_files = {}
    
    for sound_key in sounds.keys():
        minecraft_key = f"minecraft:{sound_key}"
        # Get friendly name from actiondump, null if not found
        friendly_name = sound_id_to_friendly.get(sound_key, None)
        # Get variant map for this sound
        variants = sound_id_to_variants.get(sound_key, {})
        
        # Get sounds from sounds.json
        sound_data = sounds[sound_key]
        sound_list = sound_data.get('sounds', [])
        
        # Get seeds from sound_seeds.json if available
        seed_data = sound_seeds.get(minecraft_key, {})
        seed_list = seed_data.get('sounds', [])
        
        # Create a mapping of sound IDs to seeds
        seed_map = {}
        for seed_item in seed_list:
            if isinstance(seed_item, dict):
                sound_id = seed_item.get('id', '')
                # Remove minecraft: prefix if present
                if sound_id.startswith('minecraft:'):
                    sound_id = sound_id[10:]
                seed_map[sound_id] = seed_item.get('seed')
        
        # Process each sound file
        for sound_item in sound_list:
            if isinstance(sound_item, dict):
                sound_name = sound_item.get('name', '')
            elif isinstance(sound_item, str):
                sound_name = sound_item
            else:
                continue
            
            if not sound_name:
                continue
            
            # Get the seed for this sound file
            seed = seed_map.get(sound_name)
            
            # Get seed_name from variants
            # Extract the variant ID from the sound file path
            # e.g., "ambient/nether/basalt_deltas/basaltground1" -> "basaltground1"
            variant_id = sound_name.split('/')[-1]
            seed_name = variants.get(variant_id, None)
            
            sound_files[sound_name] = {
                'key': minecraft_key,
                'seed': seed,
                'name': friendly_name,
                'seed_name': seed_name
            }
    
    print(f"Generated {len(sound_files)} sound file mappings")
    
    # Write output files
    print("\nWriting sound_names.json...")
    with open('sound_names.json', 'w') as f:
        json.dump(friendly_names, f, indent=2)
    
    print("Writing sound_files.json...")
    with open('sound_files.json', 'w') as f:
        json.dump(sound_files, f, indent=2)

    print("\n✓ Done!")
    print(f"  - sound_names.json: {len(friendly_names)} entries")
    print(f"  - sound_files.json: {len(sound_files)} entries")

if __name__ == '__main__':
    main()
