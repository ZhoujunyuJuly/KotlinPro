cmake_minimum_required(VERSION 3.6)

# 你想生成的so名字
add_library(MyLoader SHARED src/main/cpp/JNILoader.cpp)

find_library(log-lib log)

target_link_libraries(MyLoader ${log-lib})