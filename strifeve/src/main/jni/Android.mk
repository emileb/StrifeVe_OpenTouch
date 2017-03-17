
TOP_DIR := $(call my-dir)

LOCAL_PATH := $(call my-dir)

include $(TOP_DIR)/SDL2_OpenTouch/Android.mk

include $(TOP_DIR)/jwzgles/Android.mk

include $(TOP_DIR)/strife-ve/strife-ve-src/Android.mk

include $(TOP_DIR)/MobileTouchControls/Android.mk


