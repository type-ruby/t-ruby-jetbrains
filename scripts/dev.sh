#!/bin/bash
cd "$(dirname "$0")/.."

# fswatch 설치 확인
if ! command -v fswatch &> /dev/null; then
    echo "fswatch 설치 필요: brew install fswatch"
    exit 1
fi

# IDE 선택 메뉴
select_ide() {
    local options=("IntelliJ IDEA" "RubyMine" "WebStorm" "PyCharm" "GoLand")
    local ide_types=("IC" "RM" "WS" "PC" "GO")
    local selected=0
    local key

    # 커서 숨기기
    tput civis

    while true; do
        # 메뉴 출력
        echo -e "\033[1mSelect IDE:\033[0m"
        for i in "${!options[@]}"; do
            if [ $i -eq $selected ]; then
                echo -e "  \033[7m▸ ${options[$i]}\033[0m"
            else
                echo "    ${options[$i]}"
            fi
        done

        # 키 입력 읽기
        read -rsn1 key
        if [[ $key == $'\x1b' ]]; then
            read -rsn2 key
            case $key in
                '[A') # 위
                    ((selected--))
                    [ $selected -lt 0 ] && selected=$((${#options[@]} - 1))
                    ;;
                '[B') # 아래
                    ((selected++))
                    [ $selected -ge ${#options[@]} ] && selected=0
                    ;;
            esac
        elif [[ $key == "" ]]; then # Enter
            break
        fi

        # 메뉴 지우기 (옵션 개수 + 1줄)
        for i in $(seq 0 ${#options[@]}); do
            tput cuu1
            tput el
        done
    done

    # 커서 복원
    tput cnorm

    IDE_TYPE="${ide_types[$selected]}"
    IDE_NAME="${options[$selected]}"
}

# IDE 선택
select_ide
echo "Selected: $IDE_NAME"
echo ""

RUNIDE_PID=""

# 우아한 종료
cleanup() {
    echo ""
    echo "Shutting down gracefully..."

    # Gradle runIde 종료 (SIGTERM으로 우아하게)
    if [ -n "$RUNIDE_PID" ]; then
        echo "Stopping IDE..."
        kill -TERM $RUNIDE_PID 2>/dev/null

        # 최대 10초 대기
        for i in {1..10}; do
            if ! kill -0 $RUNIDE_PID 2>/dev/null; then
                break
            fi
            sleep 1
        done

        # 아직 살아있으면 강제 종료
        if kill -0 $RUNIDE_PID 2>/dev/null; then
            echo "Force killing IDE..."
            kill -9 $RUNIDE_PID 2>/dev/null
        fi
    fi

    echo "Done."
    exit 0
}
trap cleanup SIGINT SIGTERM EXIT

# runIde 백그라운드 실행 (선택한 IDE로)
echo "Starting $IDE_NAME..."
./gradlew runIde -PplatformType=$IDE_TYPE &
RUNIDE_PID=$!

# IDE 시작 대기
sleep 3

echo "Watching src/ for changes..."
echo "Press Ctrl+C to stop"

fswatch -o src/ build.gradle.kts | while read; do
    echo "Change detected, building..."
    ./gradlew buildPlugin -PplatformType=$IDE_TYPE --quiet
done
