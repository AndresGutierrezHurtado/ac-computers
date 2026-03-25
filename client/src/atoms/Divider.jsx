import React from 'react'

export default function Divider({ text, className }) {
  return (
    <div className={`divider ${className}`.trim()}>
        <span>{text}</span>
    </div>
  )
}
