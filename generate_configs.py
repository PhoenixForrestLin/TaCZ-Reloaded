import json
import os

# 根据报告的数据
config_data = {
    "ai_awp": {
        "shell_id": "taczreloaded:338",
        "delay": 3.25,
        "interruptible": True
    },
    "db_long": {
        "shell_id": "taczreloaded:12g",
        "delay": 2.3
    },
    "deagle": {
        "shell_id": "taczreloaded:50ae",
        "delay": 1.64
    },
    "deagle_golden": {
        "shell_id": "taczreloaded:357mag",
        "delay": 1.64
    },
    "glock_17": {
        "shell_id": "taczreloaded:9mm",
        "delay": 1.88
    },
    "m1014": {
        "shell_id": "taczreloaded:12g",
        "delay": 0.17
    },
    "m107": {
        "shell_id": "taczreloaded:50bmg",
        "delay": 5.25
    },
    "m1911": {
        "shell_id": "taczreloaded:45acp",
        "delay": 2.48
    },
    "m320": {
        "shell_id": "taczreloaded:40mm",
        "delay": 3.0
    },
    "m700": {
        "shell_id": "taczreloaded:30_06",
        "delay": 3.45,
        "interruptible": True
    },
    "m870": {
        "shell_id": "taczreloaded:12g",
        "delay": 0.17,
        "interruptible": True
    },
    "m95": {
        "shell_id": "taczreloaded:50bmg",
        "delay": 5.23,
        "interruptible": True
    },
    "p320": {
        "shell_id": "taczreloaded:45acp",
        "delay": 2.48
    },
    "rpg7": {
        "shell_id": "taczreloaded:rpg_rocket",
        "delay": 3.2
    },
    "sks_tactical": {
        "shell_id": "taczreloaded:762x39",
        "delay": 2.19
    },
    "springfield1873": {
        "shell_id": "taczreloaded:45_70",
        "delay": 2.5
    },
    "timeless50": {
        "shell_id": "taczreloaded:50ae",
        "delay": 1.64
    }
}

# 输出生成信息
for gun_name, config in config_data.items():
    print(f"{gun_name:<20} -> shell_id: {config['shell_id']:<25} delay: {config['delay']}s")
