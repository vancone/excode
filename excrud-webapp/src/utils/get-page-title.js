import defaultSettings from '@/settings'

const title = defaultSettings.title || 'MekCone ExCRUD'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
