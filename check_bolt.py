import json5

files = {
    'ai_awp': r'c:\Users\linyu\Documents\mcmod\tacz\run\tacz\tacz_default_gun\data\tacz\data\guns\ai_awp_data.json',
    'm700': r'c:\Users\linyu\Documents\mcmod\tacz\run\tacz\tacz_default_gun\data\tacz\data\guns\m700_data.json',
    'm107': r'c:\Users\linyu\Documents\mcmod\tacz\run\tacz\tacz_default_gun\data\tacz\data\guns\m107_data.json',
}

for name, filepath in files.items():
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            data = json5.load(f)
        print(f'\n{name.upper()}:')
        print(f'  bolt: {data.get("bolt")}')
        print(f'  fire_mode: {data.get("fire_mode")}')
        print(f'  bolt_action_time: {data.get("bolt_action_time", "N/A")}')
        print(f'  Has script_param: {"script_param" in data}')
    except Exception as e:
        print(f'Error reading {name}: {e}')
