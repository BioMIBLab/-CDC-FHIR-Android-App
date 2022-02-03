#!/bin/bash
CUDA_VISIBLE_DEVICES=0
python translate.py -model brnn_checked/demo-model_step_100000.pt -src data/src-test.txt -output output_translate/brnn_pred_100000.txt -replace_unk -verbose -gpu 0