<template>
  <div class="about">
    <h1>This is an about page</h1>
    <textarea
      placeholder="term, definition"
      class="bulk-match__editor"
      ref="textareaRef"
    ></textarea>
    <h1>END</h1>
  </div>
</template>

<script>
import { reactive, ref, toRefs } from 'vue'
import { defineComponent } from '@vue/composition-api'
import useCodeMirror from '../components/useCodeMirror.js'
export default defineComponent({
  emits: ['change', 'save', 'upload'],
  setup (props, { emit }) {
    const state = reactive({
      showWarning: false,
      confirm: false
    })

    const textareaRef = ref()

    const {
      append,
      editor,
      dirty,
      generation,
      hasErrors,
      setValue
    } = useCodeMirror({
      emit,
      options: props.options || {},
      initialValue: props.bulkMatches,
      textareaRef
    })

    const onFileChange = (event) => {
      emit('upload', { event, append })
    }

    const onSave = () => {
      if (hasErrors() && !state.confirm) {
        return (state.showWarning = true)
      }
      emit('save', { setValue })
    }

    return {
      ...toRefs(state),
      editor,
      dirty,
      generation,
      onFileChange,
      onSave,
      textareaRef
    }
  }
})
</script>

<style scoped>
.CodeMirror {
    width: 100%;
    /* border: 1px solid #cad3dc; */
    border-radius: 0.5rem;
    margin: 1rem 0;
    font-size: 1.0625rem;
    line-height: 1.5;
    text-align: left;
    font-family: "Inter", "Ubuntu Mono", monospace;
    /* color: #171c20;
    background-color: #e6f0ff; */
    z-index: 1;

    &-focused {
      border-color: rgba(23, 28, 32, 0.4);
      transition: all 300ms ease;
    }

    .CodeMirror-lines {
      padding: 8px 0;
    }

    pre {
      &.CodeMirror-line,
      &.CodeMirror-line-like {
        padding: 0 0 0 1rem;
      }
    }
  }
  </style>
