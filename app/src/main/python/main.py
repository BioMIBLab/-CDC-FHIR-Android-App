# -*- coding: utf-8 -*-
"""
Created on Sat Dec  5 21:55:49 2020

@author: UnitedHolmes
"""

#import scapy.all
import os
import pandas as pd
from fuzzywuzzy import fuzz
# import numpy as np

def convert_human_readables_to_ICD_9(ICD_9_human_readables):
    
    root_path = os.path.dirname(__file__)
    
    df = pd.read_csv(os.path.join(root_path, 'Section111ValidICD9-Jan2021.csv'), usecols=[0,1])
    
    # df = pd.read_csv('Section111ValidICD9-Jan2021.csv', usecols=[0,1])
    
    for col in df.columns:
        if col.startswith('LONG'):
            human_readable_col_name = col
            # print(col)
            
    ICD_9_codes = []
            
    for long_description in ICD_9_human_readables:
        
        max_fuzz_score = 0
        max_fuzz_idx = 0
        
        for idx in df.index:
            # if fuzz.partial_ratio(df[human_readable_col_name].loc[idx], long_description) > max_fuzz_score:
            if fuzz.partial_ratio(long_description, df[human_readable_col_name].loc[idx]) > max_fuzz_score:
                max_fuzz_score = fuzz.partial_ratio(long_description, df[human_readable_col_name].loc[idx])
                max_fuzz_idx = idx
                
        print(long_description, max_fuzz_score, df[human_readable_col_name].loc[max_fuzz_idx])
        ICD_9_codes.append(df["CODE"].loc[max_fuzz_idx])
            
#        print(df[df[human_readable_col_name] == long_description].index.values)
    
    return ICD_9_codes

def convert_ICD_10_to_human_readables(ICD_10_codes):
    
    root_path = os.path.dirname(__file__)
    
    df = pd.read_csv(os.path.join(root_path, 'Section111ValidICD10-Jan2021.csv'), usecols=[0,1])
    
    # df = pd.read_csv('Section111ValidICD10-Jan2021.csv', usecols=[0,1])
    
    for col in df.columns:
        if col.startswith('LONG'):
            human_readable_col_name = col
            print(col)
            
    ICD_10_human_readables = []
            
    for code in ICD_10_codes:
        
        print(code)
        
        idx = df[df["CODE"] == code].index.values[0]
        ICD_10_human_readables.append(df[human_readable_col_name].loc[idx])
        
    return ICD_10_human_readables
        
        
def main(data):
    
    root_path = os.path.dirname(__file__)
       
    # source_human_readable_file = os.path.join(root_path, 'source_human_readable_file.txt')
    # with open(source_human_readable_file, 'w') as file:
    #     file.writelines(data)
    
    # print(data)
    
    ICD_9_human_readables = data.split(';')
    ICD_9_codes = convert_human_readables_to_ICD_9(ICD_9_human_readables)
    
    # src_ICD_9_codes_test = os.path.join('data', 'src_ICD_9_codes_test.txt')
    src_ICD_9_codes_test = os.path.join(root_path, 'data', 'src_ICD_9_codes_test.txt')
    with open(src_ICD_9_codes_test, 'w') as file:
        for code in ICD_9_codes:
            file.write(code + ' ')
            
    # if not os.path.exists('output_translate'):
    #     os.mkdir('output_translate')
    
    # output_ICD_10_codes_test = os.path.join('output_translate', 'output_ICD_10_codes_test.txt')
    output_ICD_10_codes_test = os.path.join(root_path, 'output_translate', 'output_ICD_10_codes_test.txt')
            
    os.system('python translate.py -model brnn_checked/demo-model_step_100000.pt -src ' + src_ICD_9_codes_test + ' -output ' + output_ICD_10_codes_test + ' -replace_unk -verbose -gpu 0')
    
    with open(output_ICD_10_codes_test, 'r') as file:
        ICD_10_codes_raw = file.readline()
        
    ICD_10_codes = ICD_10_codes_raw.split()
    
    ICD_10_human_readables = convert_ICD_10_to_human_readables(ICD_10_codes)
    
    print(ICD_10_human_readables)
    
    res = ICD_10_human_readables[0]
    for i in range(1, len(ICD_10_human_readables)):
        res = res + ';' + ICD_10_human_readables[i]
    
    return res

# raw = 'Neuromuscular dysfunction of bladder, unspecified; Parapalegia, incomplete; Multiple sclerosis; Urinary tract infection, site not specified; Pressure ulcer of contiguous site of back, buttock and hip, unstageable; Parapalegia, complete; Hypotension, unspecified; Infection and inflammatory reaction due to indwelling urethral catheter, initial encounter; Severe sepsis with septic shock; Cardiac arrest, cause unspecified'

# main(raw)

# ICD_9_human_readables = raw.split(';')

# ICD_9_codes = convert_human_readables_to_ICD_9(ICD_9_human_readables)

# ICD_10_human_readables = convert_ICD_10_to_human_readables(['G35'])

# os.system('python translate.py -model brnn_checked/demo-model_step_100000.pt -src data/src-test.txt -output output_translate/brnn_pred_100000.txt -replace_unk -verbose -gpu 0')