#!/bin/bash
# Wrapper script for running Gradle Profiler benchmarks

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
SCENARIOS_FILE="${PROJECT_DIR}/profiler.scenarios"
TEMP_SCENARIOS_FILE=""
GRADLE_USER_HOME="${PROJECT_DIR}/.gradle-profiler"
PROFILER_OUTPUT_DIR="${PROJECT_DIR}/.gradle-profiler/results"

# Extract Gradle version from wrapper properties
GRADLE_VERSION=$(grep "distributionUrl" "${PROJECT_DIR}/gradle/wrapper/gradle-wrapper.properties" | sed 's/.*gradle-\(.*\)-bin.zip/\1/')

# Hardcoded sensible defaults
BENCHMARK_ITERATIONS=10
BENCHMARK_WARMUPS=3
PROFILE_ITERATIONS=1
PROFILE_WARMUPS=0

# Cleanup temp file on exit
cleanup() {
    if [ -n "$TEMP_SCENARIOS_FILE" ] && [ -f "$TEMP_SCENARIOS_FILE" ]; then
        rm -f "$TEMP_SCENARIOS_FILE"
    fi
}
trap cleanup EXIT

# Check if gradle-profiler is installed
if ! command -v gradle-profiler &> /dev/null; then
    echo "Error: gradle-profiler is not installed"
    echo ""
    echo "Install with:"
    echo "  brew install gradle-profiler  # macOS"
    echo "  sdk install gradle-profiler   # SDKMAN!"
    echo ""
    echo "Or download from: https://github.com/gradle/gradle-profiler/releases"
    exit 1
fi

# Show help if no arguments or --help flag
if [ $# -eq 0 ] || [ "$1" = "--help" ] || [ "$1" = "-h" ]; then
    echo "Gradle Profiler wrapper for Blueprint"
    echo ""
    echo "Usage:"
    echo "  $0 <scenario-name>              # Benchmark a predefined scenario"
    echo "  $0 --task <task-name>           # Profile a specific task"
    echo ""
    echo "Modes:"
    echo "  Benchmark (scenario)            ${BENCHMARK_ITERATIONS} iterations, ${BENCHMARK_WARMUPS} warmups, with cleanup"
    echo "  Profile (--task)                JFR (Java Flight Recorder) with flame graphs"
    echo ""
    echo "Gradle version: ${GRADLE_VERSION} (from wrapper)"
    echo ""
    echo "Note: Profiling uses JFR (built into JDK) and settings from gradle.properties"
    echo ""
    echo "Available benchmark scenarios:"
    echo ""
    grep "^[a-z_]* {" "${SCENARIOS_FILE}" | sed 's/ {$//' | while read -r scenario; do
        title=$(grep -A1 "^${scenario} {" "${SCENARIOS_FILE}" | grep "title =" | sed 's/.*= "\(.*\)"/\1/' || echo "${scenario}")
        printf "  %-30s %s\n" "${scenario}" "${title}"
    done
    echo ""
    echo "Examples:"
    echo ""
    echo "  Benchmarking (statistical analysis):"
    echo "    $0 config_cache_hot"
    echo "    $0 build_with_cache"
    echo "    $0 test_task"
    echo ""
    echo "  Profiling (flame graphs & detailed analysis):"
    echo "    $0 --task compileKotlin"
    echo "    $0 --task test"
    echo "    $0 --task build"
    exit 0
fi

# Parse command
TASK_TO_PROFILE=""

if [ "$1" = "--task" ]; then
    if [ -z "$2" ]; then
        echo "Error: --task requires a task name"
        exit 1
    fi
    TASK_TO_PROFILE="$2"
    shift 2

    echo "Profiling task: ${TASK_TO_PROFILE}"
    echo "  Gradle: ${GRADLE_VERSION} (from wrapper)"
    echo "  Note: Uses project gradle.properties config cache settings"
    echo ""
else
    SCENARIO=$1
    shift
fi

echo "Running Gradle Profiler..."
echo "Project: ${PROJECT_DIR}"

# Ensure output directory exists
mkdir -p "${PROFILER_OUTPUT_DIR}"

# Execute based on mode
if [ -n "$TASK_TO_PROFILE" ]; then
    # Profiling mode: pass task directly with JFR
    echo "Mode: profiling (JFR)"
    echo ""

    gradle-profiler \
        --profile jfr \
        --project-dir "${PROJECT_DIR}" \
        --gradle-user-home "${GRADLE_USER_HOME}" \
        --output-dir "${PROFILER_OUTPUT_DIR}" \
        "${TASK_TO_PROFILE}"
else
    # Benchmarking mode: use scenario file
    echo "Mode: benchmark"
    echo "Scenario: ${SCENARIO}"
    echo ""

    gradle-profiler \
        --benchmark \
        --project-dir "${PROJECT_DIR}" \
        --scenario-file "${SCENARIOS_FILE}" \
        --gradle-user-home "${GRADLE_USER_HOME}" \
        --output-dir "${PROFILER_OUTPUT_DIR}" \
        "${SCENARIO}"
fi

echo ""
echo "Results saved to: ${PROFILER_OUTPUT_DIR}"
